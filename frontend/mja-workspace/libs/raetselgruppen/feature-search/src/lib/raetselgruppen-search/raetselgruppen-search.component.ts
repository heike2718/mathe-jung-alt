import { LiveAnnouncer } from '@angular/cdk/a11y';
import { SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { initialRaetselgruppenSuchparameter, RaetselgruppeDatasource, RaetselgruppenFacade, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter, SortOrder } from '@mja-workspace/raetselgruppen/domain';
import { merge, Subscription, tap } from 'rxjs';

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

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  selection = new SelectionModel<RaetselgruppensucheTrefferItem>(false, []);

  #suchparameterSubscription: Subscription = new Subscription();
  #suchparameter!: RaetselgruppenSuchparameter;

  columnDefinitions = [NAME, LEVEL, REFERENZTYP, REFERENZ];
  dataSource!: RaetselgruppeDatasource;
  anzahlRaetselgruppen: number = 0;

  constructor(public raetselgruppenFacade: RaetselgruppenFacade, private _liveAnnouncer: LiveAnnouncer) { }

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
      this.#suchparameter = {...this.#suchparameter, pageIndex: 0};
      this.paginator.pageIndex = this.#suchparameter.pageIndex;
    });

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => this.#loadRaetselgruppen())
    ).subscribe();

    this.raetselgruppenFacade.setSuchparameter(initialRaetselgruppenSuchparameter);

  }

  ngOnDestroy(): void {
    this.#suchparameterSubscription.unsubscribe();
  }

  onRowClicked(row: any): void {

    const raetselgruppe: RaetselgruppensucheTrefferItem = <RaetselgruppensucheTrefferItem>row;
    this.raetselgruppenFacade.selectRaetselgruppe(raetselgruppe);
  }

  announceSortChange(sortState: Sort): void {

    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
      // const direction: SortOrder = sortState.direction === 'asc' ? 'asc' : 'desc';

      // if (sortState.active === NAME) {
      //   this.#suchparameter = { ...this.#suchparameter, sortName: direction };
      // }

      // if (sortState.active === LEVEL) {
      //   this.#suchparameter = { ...this.#suchparameter, sortSchwierigkeitsgrad: direction };
      // }

      // if (sortState.active === REFERENZTYP) {
      //   this.#suchparameter = { ...this.#suchparameter, sortReferenztyp: direction };
      // }

      // if (sortState.active === REFERENZ) {
      //   this.#suchparameter = { ...this.#suchparameter, sortReferenz: direction };
      // }
    } else {
      this._liveAnnouncer.announce('Sorting cleared');

      // if (sortState.active === NAME) {
      //   this.#suchparameter = { ...this.#suchparameter, sortName: 'noop' };
      // }

      // if (sortState.active === LEVEL) {
      //   this.#suchparameter = { ...this.#suchparameter, sortSchwierigkeitsgrad: 'noop' };
      // }

      // if (sortState.active === REFERENZTYP) {
      //   this.#suchparameter = { ...this.#suchparameter, sortReferenztyp: 'noop' };
      // }

      // if (sortState.active === REFERENZ) {
      //   this.#suchparameter = { ...this.#suchparameter, sortReferenz: 'noop' };
      // }
    }
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
