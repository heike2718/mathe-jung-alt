import { AfterViewInit, ChangeDetectorRef, Component, HostListener, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, SortDirection } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { AufgabensammlungenDataSource, AufgabensammlungenFacade } from '@mja-ws/aufgabensammlungen/api';
import { debounceTime, distinctUntilChanged, merge, Subscription, tap } from 'rxjs';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GuiReferenztypenMap, 
  GuiRefereztyp, 
  GuiSchwierigkeitsgrad, 
  GuiSchwierigkeitsgradeMap, 
  initialGuiReferenztyp, 
  initialGuiSchwierigkeitsgrad, 
  initialPaginationState, 
  PageDefinition, 
  PaginationState, 
  Referenztyp, 
  Schwierigkeitsgrad 
} from '@mja-ws/core/model';
import { initialAufgabensammlungenSuchparameter, 
  isInitialAufgabensammlungenSuchparameter, 
  AufgabensammlungenSuchparameter, 
  AufgabensammlungTrefferItem } from '@mja-ws/aufgabensammlungen/model';
import { MatSelectModule } from '@angular/material/select';
import { Configuration } from '@mja-ws/shared/config';

const STATUS = 'status';
const NAME = 'name';
const LEVEL = 'schwierigkeitsgrad';
const REFERENZTYP = 'referenztyp';
const REFERENZ = 'referenz';

@Component({
  selector: 'mja-aufgabensammlungen-search',
  standalone: true,
  imports: [
    CommonModule,
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
  templateUrl: './aufgabensammlungen-search.component.html',
  styleUrls: ['./aufgabensammlungen-search.component.scss'],
})
export class AufgabensammlungenSearchComponent implements OnInit, AfterViewInit, OnDestroy {

  dataSource = inject(AufgabensammlungenDataSource);
  anzahlSammlungen: number = 0;


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

  #configuration = inject(Configuration);
  #aufgabensammlungenFacade = inject(AufgabensammlungenFacade);
  

  #nameFilterSubscription = new Subscription();
  #schwierigkeitsgradFilterSubscription = new Subscription();
  #referenztypFilterSubscription = new Subscription();
  #referenzFilterSubscription = new Subscription();
  #matSortChangedSubscription: Subscription = new Subscription();
  #matPaginatorSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();


  #suchparameter: AufgabensammlungenSuchparameter = initialAufgabensammlungenSuchparameter;
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

    this.#paginationStateSubscription = this.#aufgabensammlungenFacade.paginationState$.subscribe(
      (state: PaginationState) => {
        this.anzahlSammlungen = state.anzahlTreffer;
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

    
    this.#initPaginator();

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

    // oder explizit nochmal changeDetection triggern
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

  showSuchparameter(): boolean {
    return !this.#configuration.production;
  }

  getDisplayedColumns(): string[] {
    if (this.#scrWidth > 959) {
      return [STATUS, NAME, LEVEL, REFERENZTYP, REFERENZ];
    } else {
      return [STATUS, NAME];
    }
  }

  buttonResetAllFiltersDisabled(): boolean {

    if (isInitialAufgabensammlungenSuchparameter(this.#suchparameter)) {
      return true;
    }

    return this.paginator === undefined;
  }

  neueAufgabensammlung(): void {
    this.#aufgabensammlungenFacade.createAndEditAufgabensammlung();
  }

  onRowClicked(aufgabensammlung: AufgabensammlungTrefferItem): void {
    this.#aufgabensammlungenFacade.selectAufgabensammlung(aufgabensammlung);
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

    this.#aufgabensammlungenFacade.triggerSearch(this.#suchparameter, pageDefinition)
  }
}
