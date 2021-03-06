import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Deskriptor, DeskriptorenSearchFacade, Suchkontext } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { deskriptorenToString, Quelle, QuellenDataSource, QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { Suchfilter, SuchfilterFacade } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
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
  #deskriptorenLoadedSubscription: Subscription = new Subscription();
  #canStartSucheSubscription: Subscription = new Subscription();
  #suchfilterSubscription: Subscription = new Subscription();

  suchfilter: Suchfilter | undefined;
  quelleList$ = this.quellenFacade.quellenList$;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['art', 'name', 'deskriptoren'];
  dataSource!: QuellenDataSource;
  anzahlQuellen: number = 0;

  constructor(public quellenFacade: QuellenFacade,
    public suchfilterFacade: SuchfilterFacade,
    private deskriptorenSearchFacade: DeskriptorenSearchFacade) {}

  ngOnInit() {

    this.dataSource = new QuellenDataSource(this.quellenFacade);
    
    this.#deskriptorenLoadedSubscription = this.deskriptorenSearchFacade.loaded$.subscribe(
      (loaded => {
        if (loaded) {
          this.suchfilterFacade.changeSuchkontext(this.#kontext);
        }
      })
    );

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
          this.triggerSuche();
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

  private loadQuellenPage(): void {
    this.quellenFacade.slicePage(this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }

  private triggerSuche(): void {

    if (this.suchfilter) {
      this.quellenFacade.findQuellen(this.suchfilter);
    }
  }
}
