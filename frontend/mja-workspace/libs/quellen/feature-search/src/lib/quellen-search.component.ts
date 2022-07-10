import { Component, OnDestroy, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Quelle, QuellenDataSource, QuellenFacade } from '@mja-workspace/quellen/domain';
import { Deskriptor, deskriptorenToString, Suchfilter, SuchfilterFacade, Suchkontext, suchkriterienVorhanden } from '@mja-workspace/suchfilter/domain';
import { debounceTime, distinctUntilChanged, filter, merge, Subscription, tap } from 'rxjs';

@Component({
  selector: 'mja-quellen-search',
  templateUrl: './quellen-search.component.html',
  styleUrls: ['./quellen-search.component.scss'],
})
export class QuellenSearchComponent implements OnInit, OnDestroy, AfterViewInit {

  #kontext: Suchkontext = 'QUELLEN';

  #quellenSubscription: Subscription = new Subscription();
  #sucheReadySubscription: Subscription = new Subscription();
  #sucheClearedSubscription: Subscription = new Subscription();
  #deskriptorenLoadedSubscription: Subscription = new Subscription();
  #canStartSucheSubscription: Subscription = new Subscription();
  #suchfilterSubscription: Subscription = new Subscription();
  #sortChangedSubscription: Subscription = new Subscription();
  #paginatorSubscription: Subscription = new Subscription();

  suchfilter!: Suchfilter;
  quelleList$ = this.quellenFacade.quellenList$;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['art', 'name', 'deskriptoren'];
  dataSource!: QuellenDataSource;
  anzahlQuellen: number = 0;

  constructor(public quellenFacade: QuellenFacade,
    public suchfilterFacade: SuchfilterFacade,
    private changeDetector: ChangeDetectorRef
  ) { }

  ngOnInit() {

    this.dataSource = new QuellenDataSource(this.quellenFacade);
    this.quellenFacade.checkOrLoadDeskriptoren();
    this.suchfilterFacade.setSuchfilter(this.quellenFacade.lastSuchfilter);

    this.#quellenSubscription = this.quellenFacade.quellenList$.subscribe(
      liste => this.anzahlQuellen = liste.length
    );

    this.#suchfilterSubscription = this.suchfilterFacade.selectedSuchfilter$.subscribe(
      (selectedSuchfilter) => {
        if (selectedSuchfilter) {
          this.suchfilter = selectedSuchfilter
        }
      }
    );

    this.#canStartSucheSubscription = this.suchfilterFacade.canStartSuche$.pipe(
      filter((ready) => ready),
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => {
        if (this.paginator && this.sort) {
          this.#triggerSuche();
        }
      })
    ).subscribe();

  }

  ngOnDestroy(): void {

    this.#quellenSubscription.unsubscribe();
    this.#sucheReadySubscription.unsubscribe();
    this.#sucheClearedSubscription.unsubscribe();
    this.#deskriptorenLoadedSubscription.unsubscribe();
    this.#canStartSucheSubscription.unsubscribe();
    this.#suchfilterSubscription.unsubscribe();
    this.#sortChangedSubscription.unsubscribe();
    this.#paginatorSubscription.unsubscribe();
  }

  ngAfterViewInit(): void {

    // reset the paginator after sorting
    this.#initPaginator();

    this.#paginatorSubscription = merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => {
        if (this.suchfilter !== undefined) {
          console.log(JSON.stringify(this.suchfilter));
          this.#triggerSuche();
        }
      })
    ).subscribe();

    this.changeDetector.detectChanges();
  }

  deskriptorenToString(quelle: Quelle): string {
    return deskriptorenToString(quelle.deskriptoren);
  }

  onDeskriptorenChanged(_$event: Deskriptor[]): void {

    if (this.paginator) {
      this.paginator.pageIndex = 0;
    }
  }

  onInputChanged($event: string) {
    this.paginator.pageIndex = 0;
    this.suchfilterFacade.changeSuchtext($event);
  }

  onRowClicked(row: any): void {

    const quelle: Quelle = <Quelle>row;
    this.quellenFacade.selectQuelle(quelle);
  }


  #loadQuellenPage(): void {
    this.quellenFacade.slicePage(this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }

  #triggerSuche(): void {
    this.quellenFacade.findQuellen(this.suchfilter);
  }

  #initPaginator(): void {

    // reset Paginator when sort changed
    this.#sortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  }
}
