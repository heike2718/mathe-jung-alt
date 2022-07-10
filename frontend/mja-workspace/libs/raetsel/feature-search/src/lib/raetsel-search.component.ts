import { Component, OnDestroy, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { QuellenFacade } from '@mja-workspace/quellen/domain';
import { Raetsel, RaetselDataSource, RaetselFacade } from '@mja-workspace/raetsel/domain';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { deskriptorenToString, PageDefinition, Suchfilter, SuchfilterFacade, Suchkontext, suchkriterienVorhanden } from '@mja-workspace/suchfilter/domain';
import { filter, Subscription, debounceTime, tap, merge, distinctUntilChanged } from 'rxjs';


@Component({
  selector: 'mja-raetsel-search',
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss'],
})
export class RaetselSearchComponent implements OnInit, OnDestroy, AfterViewInit {

  #kontext: Suchkontext = 'RAETSEL';

  #sucheClearedSubscription: Subscription = new Subscription();
  #userAdminSubscription: Subscription = new Subscription();
  #canStartSucheSubscription: Subscription = new Subscription();
  #suchfilterSubscription: Subscription = new Subscription();
  #sortChangedSubscription: Subscription = new Subscription();
  #paginatorSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();


  isAdmin = false;

  suchfilter!: Suchfilter;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  #columnDefinitionsPublic = ['schluessel', 'name', 'deskriptoren'];
  #columnDefinitionsAdmin = ['schluessel', 'name', 'kommentar'];

  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  constructor(public raetselFacade: RaetselFacade,
    public quellenFacade: QuellenFacade,
    private suchfilterFacade: SuchfilterFacade,
    private authFacade: AuthFacade,
    private changeDetector: ChangeDetectorRef
  ) {

    console.log('SearchComponent created');
  }

  ngOnInit() {

    this.dataSource = new RaetselDataSource(this.raetselFacade);
    this.raetselFacade.checkOrLoadDeskriptoren();
    this.suchfilterFacade.setSuchfilter(this.raetselFacade.lastSuchfilter);

    this.#paginationStateSubscription = this.raetselFacade.paginationState$.subscribe(
      (state) => this.anzahlRaetsel = state.anzahlTreffer
    );

    this.#canStartSucheSubscription = this.suchfilterFacade.canStartSuche$.pipe(
      filter((ready) => ready === true),
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => {
        if (this.paginator && this.sort && this.suchfilter) {
          this.#triggerSuche();
        }
      })
    ).subscribe();

    this.#userAdminSubscription = this.authFacade.isAdmin$.subscribe(

      admin => this.isAdmin = admin

    );

    this.#suchfilterSubscription = this.suchfilterFacade.selectedSuchfilter$.subscribe(
      (selectedSuchfilter) => {
        if (selectedSuchfilter) {
          this.suchfilter = selectedSuchfilter;
        }
      }
    );

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

  ngOnDestroy(): void {
    this.#suchfilterSubscription.unsubscribe();
    this.#sucheClearedSubscription.unsubscribe();
    this.#userAdminSubscription.unsubscribe();
    this.#canStartSucheSubscription.unsubscribe();
    this.#sortChangedSubscription.unsubscribe();
    this.#paginatorSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
  }

  getDisplayedColumns(): string[] {
    return this.isAdmin ? this.#columnDefinitionsAdmin : this.#columnDefinitionsPublic;
  }

  onDeskriptorenChanged($event: any): void {

    if (this.paginator) {
      this.paginator.pageIndex = 0;
    }
    if (this.suchfilter !== undefined) {
      this.#triggerSuche();
    }
  }

  onInputChanged($event: any) {
    this.paginator.pageIndex = 0;
    this.suchfilterFacade.changeSuchtext($event);
  }

  onRowClicked(row: any): void {

    const raetsel: Raetsel = <Raetsel>row;
    this.raetselFacade.selectRaetsel(raetsel);
  }

  deskriptorenToString(raetsel: Raetsel): string {

    return deskriptorenToString(raetsel.deskriptoren);

  }

  neueSuche(): void {
    this.raetselFacade.clearTrefferliste();
    this.suchfilterFacade.resetSuchfilter(this.#kontext);
  }

  neuesRaetsel(): void {
    this.raetselFacade.createAndEditRaetsel();
  }

  quelleWaehlen(): void {
    this.quellenFacade.navigateToQuellensuche();
  }


  #triggerSuche(): void {

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ? this.paginator.pageIndex : 0,
      pageSize: this.paginator ? this.paginator.pageSize : 10,
      sortDirection: this.sort ? this.sort.direction : 'asc'
    }

    this.raetselFacade.triggerSearch(this.suchfilter, pageDefinition);
  }

  #initPaginator(): void {

    // reset Paginator when sort changed
    this.#sortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  }
}
