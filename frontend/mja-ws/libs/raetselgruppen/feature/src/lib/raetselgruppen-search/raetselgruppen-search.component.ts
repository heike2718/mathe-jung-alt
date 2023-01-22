import { AfterViewInit, ChangeDetectorRef, Component, HostListener, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, SortDirection } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { RaetselgruppenDataSource, RaetselgruppenFacade } from '@mja-ws/raetselgruppen/api';
import { debounceTime, distinctUntilChanged, merge, Subscription, tap } from 'rxjs';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GuiReferenztypenMap, GuiRefereztyp, GuiSchwierigkeitsgrad, GuiSchwierigkeitsgradeMap, initialGuiReferenztyp, initialGuiSchwierigkeitsgrad, initialPaginationState, PageDefinition, PaginationState, Referenztyp, Schwierigkeitsgrad, SortOrder } from '@mja-ws/core/model';
import { initialRaetselgruppenSuchparameter, isInitialRaetselgruppenSuchparameter, RaetselgruppenSuchparameter, RaetselgruppenTrefferItem } from '@mja-ws/raetselgruppen/model';
import { MatSelectModule } from '@angular/material/select';

const STATUS = 'status';
const NAME = 'name';
const LEVEL = 'schwierigkeitsgrad';
const REFERENZTYP = 'referenztyp';
const REFERENZ = 'referenz';

@Component({
  selector: 'mja-raetselgruppen-search',
  standalone: true,
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatTableModule,
    MatButtonModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './raetselgruppen-search.component.html',
  styleUrls: ['./raetselgruppen-search.component.scss'],
})
export class RaetselgruppenSearchComponent implements OnInit, AfterViewInit, OnDestroy {

  dataSource = inject(RaetselgruppenDataSource);
  anzahlRaetselgruppen = 0;

  suchparameterStr = '';

  schwierigkeitsgrade: GuiSchwierigkeitsgrad[] = new GuiSchwierigkeitsgradeMap().toGuiArray();
  referenztypen: GuiRefereztyp[] = new GuiReferenztypenMap().toGuiArray();

  filterValues = {
    name: '',
    schwierigkeitsgrad: '',
    referenztyp: '',
    referenz: ''
  };

  /** controls for the MatSelect filter keyword */
  nameFilterControl = new FormControl('');
  referenzFilterControl = new FormControl('');
  schwierigkeitsgradSelectFilterControl: FormControl = new FormControl();
  referenztypSelectFilterControl: FormControl = new FormControl();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  #raetselgruppenFacade = inject(RaetselgruppenFacade);
  

  #nameFilterSubscription = new Subscription();
  #schwierigkeitsgradFilterSubscription = new Subscription();
  #referenztypFilterSubscription = new Subscription();
  #referenzFilterSubscription = new Subscription();
  #matSortChangedSubscription: Subscription = new Subscription();
  #matPaginatorSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();


  #suchparameter: RaetselgruppenSuchparameter = initialRaetselgruppenSuchparameter;
  #paginationState: PaginationState = initialPaginationState;
  #pageIndex = 0;
  #sortDirection: SortDirection = 'asc';
  #adjusting = false;

  // Declare height and width variables
  #scrWidth = window.innerWidth;

  @HostListener('window:resize', ['$event'])
  getScreenSize() {
    // this.#scrHeight = window.innerHeight;
    this.#scrWidth = window.innerWidth;
  }

  constructor(private changeDetector: ChangeDetectorRef) {
    this.schwierigkeitsgradSelectFilterControl.setValue(initialGuiSchwierigkeitsgrad);
    this.referenztypSelectFilterControl.setValue(initialGuiReferenztyp);
  }

  ngOnInit(): void {

    this.#paginationStateSubscription = this.#raetselgruppenFacade.paginationState$.subscribe(
      (state: PaginationState) => {
        this.anzahlRaetselgruppen = state.anzahlTreffer;
        this.#pageIndex = state.pageDefinition.pageIndex;
        this.#sortDirection = state.pageDefinition.sortDirection === 'asc' ? 'asc' : 'desc';
      }
    );

    this.#triggerSearch();
  }

  ngAfterViewInit(): void {

    // fixes NG0100: Expression has changed after it was checked 
    // https://angular.io/errors/NG0100
    setTimeout(() => {

      // this.#initPaginator();
      // hier den init-Kram oder
    }, 0);

    // oder explizit nochmal changeDetection triggern
    this.#initPaginator();

    this.#matPaginatorSubscription = merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => {
        this.#triggerSearch();
      })
    ).subscribe();

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => {
        this.#paginationState = { ...this.#paginationState, pageDefinition: { ...this.#paginationState.pageDefinition, sortDirection: this.#sortDirection } };
        this.#triggerSearch();
      })
    ).subscribe();

    this.#nameFilterSubscription = this.nameFilterControl.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap((name) => {

        if (name) {
          if (name.trim().length > 0) {
            this.#suchparameter = { ...this.#suchparameter, name: name };
          } else {
            this.#suchparameter = { ...this.#suchparameter, name: null };
          }
        } else {
          this.#suchparameter = { ...this.#suchparameter, name: null };
        }

        // reset Paginator
        this.paginator.pageIndex = 0;
        this.suchparameterStr = JSON.stringify(this.#suchparameter);
        this.#triggerSearch();
      })
    ).subscribe();

    this.#schwierigkeitsgradFilterSubscription = this.schwierigkeitsgradSelectFilterControl.valueChanges.pipe(

      tap((level: GuiSchwierigkeitsgrad) => {

        const schwierigkeitsgrad: Schwierigkeitsgrad = level.id;

        if (schwierigkeitsgrad !== 'NOOP') {
          this.#suchparameter = { ...this.#suchparameter, schwierigkeitsgrad: level.id };
        } else {
          this.#suchparameter = { ...this.#suchparameter, schwierigkeitsgrad: null };
        }
        // reset Paginator
        this.paginator.pageIndex = 0;
        this.suchparameterStr = JSON.stringify(this.#suchparameter);
        this.#triggerSearch();
      })
    ).subscribe();

    this.#referenztypFilterSubscription = this.referenztypSelectFilterControl.valueChanges.pipe(
      tap((reftyp: GuiRefereztyp) => {

        const referenztyp: Referenztyp = reftyp.id;
        if (referenztyp !== 'NOOP') {
          this.#suchparameter = { ...this.#suchparameter, referenztyp: reftyp.id }
        } else {
          this.#suchparameter = { ...this.#suchparameter, referenztyp: null }
        }
        // reset Paginator
        this.paginator.pageIndex = 0;
        this.suchparameterStr = JSON.stringify(this.#suchparameter);
        this.#triggerSearch();
      })
    ).subscribe();

    this.#referenzFilterSubscription = this.referenzFilterControl.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap((referenz) => {


        if (referenz) {
          if (referenz.trim().length > 0) {
            this.#suchparameter = { ...this.#suchparameter, referenz: referenz };
          } else {
            this.#suchparameter = { ...this.#suchparameter, referenz: null };
          }
        } else {
          this.#suchparameter = { ...this.#suchparameter, referenz: null };
        }

        // reset Paginator
        this.paginator.pageIndex = 0;
        this.suchparameterStr = JSON.stringify(this.#suchparameter);
        this.#triggerSearch();
      })
    ).subscribe();

    this.changeDetector.detectChanges();
  }


  ngOnDestroy(): void {
    this.#nameFilterSubscription.unsubscribe();
    this.#schwierigkeitsgradFilterSubscription.unsubscribe();
    this.#referenztypFilterSubscription.unsubscribe();
    this.#referenzFilterSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
    this.#matPaginatorSubscription.unsubscribe();
    this.#matSortChangedSubscription.unsubscribe();
  }

  getDisplayedColumns(): string[] {
    if (this.#scrWidth > 959) {
      return [STATUS, NAME, LEVEL, REFERENZTYP, REFERENZ];
    } else {
      return [STATUS, NAME];
    }
  }

  buttonResetAllFiltersDisabled(): boolean {

    if (isInitialRaetselgruppenSuchparameter(this.#suchparameter)) {
      return true;
    }

    return this.paginator === undefined;
  }

  neueRaetselgruppe(): void {
    this.#raetselgruppenFacade.createAndEditRaetselgruppe();
  }

  onRowClicked(raetselgruppe: RaetselgruppenTrefferItem): void {
    this.#raetselgruppenFacade.selectRaetselgruppe(raetselgruppe);
  }

  clearFilter(filterFormControl: FormControl): void {
    filterFormControl.patchValue('');
  }

  resetAllFilters(): void {

    this.#adjusting = true;

    this.nameFilterControl.patchValue('');
    this.referenzFilterControl.patchValue('');
    this.schwierigkeitsgradSelectFilterControl.patchValue('NOOP');

    this.#adjusting = false;

    this.referenztypSelectFilterControl.patchValue('NOOP');
  }

  #initPaginator(): void {

    this.paginator.pageIndex = this.#pageIndex;
    this.sort.direction = this.#sortDirection;
    // reset Paginator when sort changed    
    this.#matSortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  }

  #triggerSearch(): void {

    if (this.#adjusting) {
      return;
    }

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ? this.paginator.pageIndex : this.#pageIndex,
      pageSize: this.paginator ? this.paginator.pageSize : 20,
      sortDirection: this.sort ? this.sort.direction : this.#sortDirection
    }

    this.#raetselgruppenFacade.triggerSearch(this.#suchparameter, pageDefinition)
  }
}
