import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { deskriptorenToString, Quelle, QuellenDataSource, QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { SuchfilterFacade, Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { debounceTime, distinctUntilChanged, filter, map, merge, Subscription, tap } from 'rxjs';

@Component({
  selector: 'mja-quellen-search',
  templateUrl: './quellen-search.component.html',
  styleUrls: ['./quellen-search.component.scss'],
})
export class QuellenSearchComponent implements OnInit, OnDestroy {

  #kontext: Suchkontext = 'QUELLEN';
  
  #quellenSubscription: Subscription = new Subscription();
  #sucheReadySubscription: Subscription = new Subscription();
  #sucheClearedSubscription: Subscription = new Subscription();

  suchfilterWithStatus$ = this.suchfilterFacade.suchfilterWithStatus$;
  quelleList$ = this.quellenFacade.quellenList$;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['art', 'name', 'deskriptoren'];
  dataSource!: QuellenDataSource;
  anzahlQuellen: number = 0;

  constructor(public quellenFacade: QuellenFacade
    , private suchfilterFacade: SuchfilterFacade
    , private authFacade: AuthFacade) {}

  ngOnInit() {

    this.dataSource = new QuellenDataSource(this.quellenFacade);
    this.suchfilterFacade.changeSuchkontext(this.#kontext);

    this.#quellenSubscription = this.quellenFacade.quellenList$.subscribe(
      liste => this.anzahlQuellen = liste.length
    );

    this.#sucheReadySubscription = this.suchfilterFacade.suchfilterWithStatus$.pipe(
      filter((sws) => sws.suchfilter.kontext === this.#kontext && sws.nichtLeer),
      map((sws) => sws.suchfilter),
      filter(sws => sws.suchstring.length > 1),
      debounceTime(300),
      distinctUntilChanged(),
      tap((suchfilter) => this.quellenFacade.findQuellen(suchfilter))
    ).subscribe();

    this.#sucheClearedSubscription = this.suchfilterFacade.suchfilterWithStatus$.pipe(
      filter((sws) => sws.suchfilter.kontext === this.#kontext &&  !sws.nichtLeer),
      tap(() => this.quellenFacade.clearTrefferliste())
    ).subscribe();
  }

  ngOnDestroy(): void {
    
    this.#quellenSubscription.unsubscribe();
    this.#sucheReadySubscription.unsubscribe();
    this.#sucheClearedSubscription.unsubscribe();
  }

  ngAfterViewInit(): void {

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => this.loadQuellenPage())
    ).subscribe();
  }

  deskriptorenToString(quelle: Quelle): string {

    return deskriptorenToString(quelle.deskriptoren);

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

  onRowClicked(row: any): void {

    const quelle: Quelle = <Quelle>row;
    this.quellenFacade.selectQuelle(quelle);
  }

  private loadQuellenPage(): void {
    this.quellenFacade.slicePage(this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
