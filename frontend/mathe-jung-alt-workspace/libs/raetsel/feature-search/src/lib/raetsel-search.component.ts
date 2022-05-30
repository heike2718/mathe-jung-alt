import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Raetsel, RaetselDataSource, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { debounceTime, filter, map, tap } from 'rxjs/operators';
import { merge, Subscription } from 'rxjs';
import { PaginationState, SuchfilterFacade, Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { deskriptorenToString } from '@mathe-jung-alt-workspace/quellen/domain';

@Component({
  selector: 'mja-raetsel-search',
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss'],
})
export class RaetselSearchComponent implements OnInit, AfterViewInit, OnDestroy {

  #kontext: Suchkontext = 'RAETSEL';

  #sucheReadySubscription: Subscription = new Subscription();
  #sucheClearedSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();

  isAdmin$ = this.authFacade.isAdmin$;
  isOrdinaryUser$ = this.authFacade.isOrdinaryUser$;
  suchfilterWithStatus$ = this.suchfilterFacade.suchfilterWithStatus$;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['schluessel', 'name', 'deskriptoren'];
  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  constructor(public raetselFacade: RaetselFacade, private suchfilterFacade: SuchfilterFacade, private authFacade: AuthFacade) { }

  ngOnInit() {
    this.dataSource = new RaetselDataSource(this.raetselFacade);
    this.suchfilterFacade.changeSuchkontext(this.#kontext);

    this.#paginationStateSubscription = this.raetselFacade.paginationState$.subscribe(
      (state: PaginationState) => this.anzahlRaetsel = state.anzahlTreffer
    )

    this.#sucheReadySubscription = this.suchfilterFacade.suchfilterWithStatus$.pipe(
      filter((sws) => sws.suchfilter.kontext === this.#kontext && sws.nichtLeer),
      debounceTime(300),
      tap(() => this.triggerSuche())
    ).subscribe();

    this.#sucheClearedSubscription = this.suchfilterFacade.suchfilterWithStatus$.pipe(
      filter((sws) => sws.suchfilter.kontext === this.#kontext && !sws.nichtLeer),
      tap(() => this.raetselFacade.clearTrefferliste())
    ).subscribe();
  }

  onDeskriptorenChanged($event: Deskriptor[]): void {

    if (this.paginator) {
      this.paginator.pageIndex = 0;
    }
    this.suchfilterFacade.changeDeskriptoren($event);
  }

  onInputChanged($event: string) {
    this.paginator.pageIndex = 0;
    this.suchfilterFacade.changeSuchtext($event);
  }

  ngAfterViewInit(): void {

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => this.triggerSuche())
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.#sucheReadySubscription.unsubscribe();
    this.#sucheClearedSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
  }

  onRowClicked(row: any): void {

    const raetsel: Raetsel = <Raetsel>row;
    this.raetselFacade.selectRaetsel(raetsel);
  }

  deskriptorenToString(raetsel: Raetsel): string {

    return deskriptorenToString(raetsel.deskriptoren);

  }

  private triggerSuche(): void {
    this.raetselFacade.triggerSearch({ 
      pageIndex: this.paginator.pageIndex,
      pageSize: this.paginator.pageSize,
      sortDirection: this.sort.direction });
  }
}
