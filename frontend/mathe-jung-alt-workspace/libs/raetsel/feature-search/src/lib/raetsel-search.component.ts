import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Raetsel, RaetselDataSource, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { debounceTime, distinctUntilChanged, filter, tap } from 'rxjs/operators';
import { combineLatest, merge, Subscription } from 'rxjs';
import { PageDefinition, PaginationState, Suchfilter, SuchfilterFacade, Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { deskriptorenToString, QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';

@Component({
  selector: 'mja-raetsel-search',
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss'],
})
export class RaetselSearchComponent implements OnInit, AfterViewInit, OnDestroy {

  #kontext: Suchkontext = 'RAETSEL';

  #sucheClearedSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();
  #userRoleSubscription: Subscription = new Subscription();
  #deskriptorenLoadedSubscription: Subscription = new Subscription();
  #canStartSucheSubscription: Subscription = new Subscription();
  #suchfilterSubscription: Subscription = new Subscription();

  isAdmin = false;
  isOrdinaryUser = false;
  suchfilter: Suchfilter | undefined;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  #columnDefinitionsPublic = ['schluessel', 'name', 'deskriptoren'];
  #columnDefinitionsAdmin = ['schluessel', 'name', 'kommentar'];

  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  constructor(public raetselFacade: RaetselFacade,
    public suchfilterFacade: SuchfilterFacade,
    public quellenFacade: QuellenFacade,
    private authFacade: AuthFacade
  ) { }

  ngOnInit() {
    this.dataSource = new RaetselDataSource(this.raetselFacade);

    this.#deskriptorenLoadedSubscription = this.suchfilterFacade.deskriptorenLoaded$.subscribe(
      (loaded => {
        if (loaded) {
          this.suchfilterFacade.changeSuchkontext(this.#kontext);
        }
      })
    );

    this.#paginationStateSubscription = this.raetselFacade.paginationState$.subscribe(
      (state: PaginationState) => this.anzahlRaetsel = state.anzahlTreffer
    );

    this.#suchfilterSubscription = this.suchfilterFacade.selectedSuchfilter$.subscribe(
      (selectedSuchfilter) => {
        if (selectedSuchfilter) {
          this.suchfilter = selectedSuchfilter;
        }
      }
    );

    this.#canStartSucheSubscription = this.suchfilterFacade.canStartSuche$.pipe(
      filter((ready) => ready),
      debounceTime(300),
      // distinctUntilChanged(),
      tap(() => {
        if (this.paginator && this.sort) {
          this.triggerSuche();
        }
      })
    ).subscribe();

    this.#userRoleSubscription = this.authFacade.getUser$.subscribe(
      user => {
        if (user) {
          if (user.rolle === 'ADMIN') {
            this.isAdmin = true;
          } else {
            this.isOrdinaryUser = true;
          }
        }
      }
    );
  }

  onDeskriptorenChanged(_$event: Deskriptor[]): void {

    if (this.paginator) {
      this.paginator.pageIndex = 0;
    }
    this.triggerSuche();
  }

  onInputChanged($event: string) {
    this.paginator.pageIndex = 0;
    this.suchfilterFacade.changeSuchtext($event);
  }

  quelleWaehlen(): void {
    this.quellenFacade.navigateToQuellensuche();
  }

  ngAfterViewInit(): void {

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => this.triggerSuche())
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.#suchfilterSubscription.unsubscribe();
    this.#sucheClearedSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
    this.#userRoleSubscription.unsubscribe();
    this.#deskriptorenLoadedSubscription.unsubscribe();
    this.#canStartSucheSubscription.unsubscribe();
  }

  getDisplayedColumns(): string[] {
    return this.isAdmin ? this.#columnDefinitionsAdmin : this.#columnDefinitionsPublic;
  }

  onRowClicked(row: any): void {

    const raetsel: Raetsel = <Raetsel>row;
    this.raetselFacade.selectRaetsel(raetsel);
  }

  deskriptorenToString(raetsel: Raetsel): string {

    return deskriptorenToString(raetsel.deskriptoren);

  }

  neuesRaetsel(): void {
    this.raetselFacade.createAndEditRaetsel();
  }

  private triggerSuche(): void {

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ?  this.paginator.pageIndex : 0,
      pageSize: this.paginator ? this.paginator.pageSize : 10,
      sortDirection: this.sort ? this.sort.direction : 'asc'
    }

    this.raetselFacade.triggerSearch(pageDefinition);
  }
}


