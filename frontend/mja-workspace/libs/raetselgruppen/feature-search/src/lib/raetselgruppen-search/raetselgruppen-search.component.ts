import { LiveAnnouncer } from '@angular/cdk/a11y';
import { SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { initialRaetselgruppenSuchparameter, RaetselgruppeDatasource, RaetselgruppenFacade, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter, Referenztyp, Schwierigkeitsgrad, SortOrder } from '@mja-workspace/raetselgruppen/domain';
import { debounceTime, distinctUntilChanged, merge, Subscription, tap } from 'rxjs';

const STATUS = 'status';
const NAME = 'name';
const LEVEL = 'schwierigkeitsgrad';
const REFERENZTYP = 'referenztyp';
const REFERENZ = 'referenz';

@Component({
  selector: 'mja-raetselgruppen-search',
  templateUrl: './raetselgruppen-search.component.html',
  styleUrls: ['./raetselgruppen-search.component.scss'],
})
export class RaetselgruppenSearchComponent implements OnInit, AfterViewInit, OnDestroy {

  // Declare height and width variables
  // #scrHeight: number;
  #scrWidth!: number;

  @HostListener('window:resize', ['$event'])
  getScreenSize() {
    // this.#scrHeight = window.innerHeight;
    this.#scrWidth = window.innerWidth;
  }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  selection = new SelectionModel<RaetselgruppensucheTrefferItem>(false, []);

  #suchparameterSubscription: Subscription = new Subscription();
  #suchparameter!: RaetselgruppenSuchparameter;

  #nameFilterSubscription = new Subscription();
  #schwierigkeitsgradFilterSubscription = new Subscription();
  #referenztypFilterSubscription = new Subscription();
  #referenzFilterSubscription = new Subscription();

  columnDefinitions = [STATUS, NAME, LEVEL, REFERENZTYP, REFERENZ];
  dataSource!: RaetselgruppeDatasource;
  anzahlRaetselgruppen: number = 0;

  schwierigkeitsgrade: Schwierigkeitsgrad[] = ['IKID', 'EINS', 'ZWEI', 'VORSCHULE', 'EINS_ZWEI', 'DREI_VIER', 'FUENF_SECHS', 'SIEBEN_ACHT', 'AB_NEUN', 'GRUNDSCHULE', 'SEK_1', 'SEK_2'];
  referenztypen: Referenztyp[] = ['MINIKAENGURU', 'SERIE'];

  filterValues = {
    name: '',
    schwierigkeitsgrad: '',
    referenztyp: '',
    referenz: ''
  };



  nameFilterControl = new FormControl('');
  referenzFilterControl = new FormControl('');

  /** controls for the MatSelect filter keyword */
  schwierigkeitsgradSelectFilterControl: FormControl = new FormControl();
  referenztypSelectFilterControl: FormControl = new FormControl();

  constructor(public raetselgruppenFacade: RaetselgruppenFacade, private _liveAnnouncer: LiveAnnouncer) {
    this.getScreenSize();
  }

  ngOnInit(): void {

    this.dataSource = new RaetselgruppeDatasource(this.raetselgruppenFacade);

    this.#suchparameterSubscription = this.raetselgruppenFacade.suchparameter$.pipe(
      tap((state) => {
        this.#suchparameter = state;
      })
    ).subscribe();
  }

  ngAfterViewInit(): void {

    this.sort.sortChange.subscribe(() => {
      this.#suchparameter = { ...this.#suchparameter, pageIndex: 0 };
      this.paginator.pageIndex = this.#suchparameter.pageIndex;
    });

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => {
        const sortOrder: SortOrder = this.sort.direction === 'asc' ? 'asc' : 'desc'
        this.#suchparameter = { ...this.#suchparameter, sortAttribute: this.sort.active, sortOrder: sortOrder }
        this.#loadRaetselgruppen();
      })
    ).subscribe();

    this.#nameFilterSubscription = this.nameFilterControl.valueChanges.pipe(
      debounceTime(800),
      distinctUntilChanged(),
      tap((name) => {
        name ? this.#suchparameter = { ...this.#suchparameter, name: name, pageIndex: 0 } : { ...this.#suchparameter, name: null, pageIndex: 0 };
        this.#loadRaetselgruppen();
      })
    ).subscribe();

    this.#schwierigkeitsgradFilterSubscription = this.schwierigkeitsgradSelectFilterControl.valueChanges.pipe(
      debounceTime(800),
      distinctUntilChanged(),
      tap(() => { })
    ).subscribe();

    this.#referenztypFilterSubscription = this.referenztypSelectFilterControl.valueChanges.pipe(
      debounceTime(800),
      distinctUntilChanged(),
      tap(() => { })
    ).subscribe();

    this.#referenzFilterSubscription = this.referenzFilterControl.valueChanges.pipe(
      debounceTime(800),
      distinctUntilChanged(),
      tap((referenz) => {
        referenz ? this.#suchparameter = { ...this.#suchparameter, referenz: referenz, pageIndex: 0 } : { ...this.#suchparameter, referenz: null, pageIndex: 0 };
        this.#loadRaetselgruppen();
      })
    ).subscribe();

    this.raetselgruppenFacade.setSuchparameter(initialRaetselgruppenSuchparameter);
  }

  ngOnDestroy(): void {
    this.#suchparameterSubscription.unsubscribe();
    this.#nameFilterSubscription.unsubscribe();
    this.#schwierigkeitsgradFilterSubscription.unsubscribe();
    this.#referenztypFilterSubscription.unsubscribe();
    this.#referenzFilterSubscription.unsubscribe();
  }

  getDisplayedColumns(): string[] {
    if (this.#scrWidth > 959) {
      return [STATUS, NAME, LEVEL, REFERENZTYP, REFERENZ];
    } else {
      return [STATUS, NAME, LEVEL];
    }
  }

  onRowClicked(row: any): void {

    const raetselgruppe: RaetselgruppensucheTrefferItem = <RaetselgruppensucheTrefferItem>row;
    this.raetselgruppenFacade.selectRaetselgruppe(raetselgruppe);
  }

  announceSortChange(sortState: Sort): void {

    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  clearFilter(filterFormControl: FormControl): void {
    filterFormControl.patchValue('');
  }

  #loadRaetselgruppen(): void {

    if (this.#suchparameter) {
      console.log(this.#suchparameter);
      this.raetselgruppenFacade.setSuchparameter(this.#suchparameter);
    } else {
      this.raetselgruppenFacade.setSuchparameter(initialRaetselgruppenSuchparameter);
    }
  }
}
