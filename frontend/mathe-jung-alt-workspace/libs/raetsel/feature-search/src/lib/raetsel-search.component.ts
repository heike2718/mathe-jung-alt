import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { RaetselDataSource, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { debounceTime, filter, map, switchMap, tap } from 'rxjs/operators';
import { merge, Subscription } from 'rxjs';
import { SuchfilterFacade, Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';

@Component({
  selector: 'mja-raetsel-search',
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss'],
})
export class RaetselSearchComponent implements OnInit, AfterViewInit, OnDestroy {

  #kontext: Suchkontext = 'RAETSEL';

  isAdmin$ = this.authFacade.isAdmin$;
  isOrdinaryUser$ = this.authFacade.isOrdinaryUser$;
  suchfilterWithStatus$ = this.suchfilterFacade.suchfilterWithStatus$;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['schluessel', 'name', 'deskriptoren'];
  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  private raetselSubscription: Subscription = new Subscription();
  private sucheReadySubscription: Subscription = new Subscription();
  private sucheClearedSubscription: Subscription = new Subscription();

  constructor(public raetselFacade: RaetselFacade, private suchfilterFacade: SuchfilterFacade, private authFacade: AuthFacade) { }

  ngOnInit() {
    this.dataSource = new RaetselDataSource(this.raetselFacade);
    this.suchfilterFacade.changeSuchkontext(this.#kontext);

    this.raetselSubscription = this.raetselFacade.raetselList$.subscribe(
      liste => this.anzahlRaetsel = liste.length
    );

    this.sucheReadySubscription = this.suchfilterFacade.suchfilterWithStatus$.pipe(
      filter((sws) => sws.suchfilter.kontext === this.#kontext && sws.nichtLeer),
      map((sws) => sws.suchfilter),
      debounceTime(300),
      tap((suchfilter) => this.raetselFacade.findRaetselWithFilter(suchfilter))
    ).subscribe();

    this.sucheClearedSubscription = this.suchfilterFacade.suchfilterWithStatus$.pipe(
      filter((sws) => sws.suchfilter.kontext === this.#kontext &&  !sws.nichtLeer),
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
      tap(() => this.loadRaetselPage())
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.raetselSubscription.unsubscribe();
    this.sucheReadySubscription.unsubscribe();
    this.sucheClearedSubscription.unsubscribe();
  }

  onRowClicked(row: any): void {
    console.log('row clicked: ' + JSON.stringify(row));
  }

  private loadRaetselPage(): void {
    this.raetselFacade.slicePage(this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
