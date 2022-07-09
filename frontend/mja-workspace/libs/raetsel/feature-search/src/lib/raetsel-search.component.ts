import { Component, OnDestroy, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Raetsel, RaetselDataSource, RaetselSearchFacade } from '@mja-workspace/raetsel/domain';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { deskriptorenToString, PageDefinition, PaginationState, Suchfilter, SuchfilterFacade, Suchkontext } from '@mja-workspace/suchfilter/domain';
import { filter, Subscription, debounceTime, tap, merge } from 'rxjs';


@Component({
  selector: 'mja-raetsel-search',
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss'],
})
export class RaetselSearchComponent implements OnInit, OnDestroy, AfterViewInit {

  #kontext: Suchkontext = 'RAETSEL';

  #sucheClearedSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();
  #userAdminSubscription: Subscription = new Subscription();
  #deskriptorenLoadedSubscription: Subscription = new Subscription();
  #canStartSucheSubscription: Subscription = new Subscription();
  #suchfilterSubscription: Subscription = new Subscription();
  #sortChangedSubscription: Subscription = new Subscription();
  #paginatorSubscription: Subscription = new Subscription();

  isAdmin = false;

  suchfilter: Suchfilter | undefined;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  #columnDefinitionsPublic = ['schluessel', 'name', 'deskriptoren'];
  #columnDefinitionsAdmin = ['schluessel', 'name', 'kommentar'];

  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  constructor(public searchFacade: RaetselSearchFacade,
    private suchfilterFacade: SuchfilterFacade,
    private authFacade: AuthFacade,
    private changeDetector: ChangeDetectorRef
  ) {

    console.log('SearchComponent created');
  }

  ngOnInit() {

    this.dataSource = new RaetselDataSource(this.searchFacade);

    this.#deskriptorenLoadedSubscription = this.suchfilterFacade.deskriptorenLoaded$.subscribe(
      (loaded => {
        if (loaded) {
          this.suchfilterFacade.changeSuchkontext(this.#kontext);
        }
      })
    );

    this.#paginationStateSubscription = this.searchFacade.paginationState$.subscribe(
      (state: PaginationState) => this.anzahlRaetsel = state.anzahlTreffer
    );    

    this.#canStartSucheSubscription = this.suchfilterFacade.canStartSuche$.pipe(
      filter((ready) => ready),
      debounceTime(300),
      // distinctUntilChanged(),
      tap(() => {
        if (this.paginator && this.sort && this.suchfilter) {
          this.#triggerSuche(this.suchfilter);
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
          // this.#triggerSuche(this.suchfilter);
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
          this.#triggerSuche(this.suchfilter);
        }
      })
    ).subscribe();

    this.changeDetector.detectChanges();


  }

  ngOnDestroy(): void {
    this.#suchfilterSubscription.unsubscribe();
    this.#sucheClearedSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
    this.#userAdminSubscription.unsubscribe();
    this.#deskriptorenLoadedSubscription.unsubscribe();
    this.#canStartSucheSubscription.unsubscribe();
    this.#sortChangedSubscription.unsubscribe();
    this.#paginatorSubscription.unsubscribe();
  }

  getDisplayedColumns(): string[] {
    return this.isAdmin ? this.#columnDefinitionsAdmin : this.#columnDefinitionsPublic;
  }

  onDeskriptorenChanged($event: any): void {

    if (this.paginator) {
      this.paginator.pageIndex = 0;
    }
    if (this.suchfilter !== undefined) {
      this.#triggerSuche(this.suchfilter);
    }
  }

  onInputChanged($event: any) {
    this.paginator.pageIndex = 0;
    this.suchfilterFacade.changeSuchtext($event);
  }

  onRowClicked(row: any): void {

    const raetsel: Raetsel = <Raetsel>row;
    this.searchFacade.selectRaetsel(raetsel);
  }

  deskriptorenToString(raetsel: Raetsel): string {

    return deskriptorenToString(raetsel.deskriptoren);

  }

  neuesRaetsel(): void {
    // this.searchFacade.createAndEditRaetsel();
    console.log('trigger navigation');
  }


  #triggerSuche(suchfilter: Suchfilter): void {

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ? this.paginator.pageIndex : 0,
      pageSize: this.paginator ? this.paginator.pageSize : 10,
      sortDirection: this.sort ? this.sort.direction : 'asc'
    }

    this.searchFacade.triggerSearch(suchfilter, pageDefinition);
  }

  #initPaginator(): void {

    // reset Paginator when sort changed
    this.#sortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  }
}
