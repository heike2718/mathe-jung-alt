import { AfterViewInit, ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, SortDirection } from '@angular/material/sort';
import { Quelle, QuellenFacade } from '@mja-workspace/quellen/domain';
import { Raetsel, , RaetselFacade } from '@mja-workspace/raetsel/domain';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { STORAGE_KEY_QUELLE } from '@mja-workspace/shared/util-configuration';
import { deskriptorenToString, PageDefinition, Suchfilter, SuchfilterFacade, Suchkontext, suchkriterienVorhanden } from '@mja-workspace/suchfilter/domain';
import { combineLatest, debounceTime, distinctUntilChanged, filter, merge, Subscription, tap } from 'rxjs';


@Component({
  selector: 'mja-raetsel-search',
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss'],
})
export class RaetselSearchComponent implements OnInit, OnDestroy, AfterViewInit {

  #userAdminSubscription: Subscription = new Subscription();
  #canStartSucheSubscription: Subscription = new Subscription();
  #suchfilterSubscription: Subscription = new Subscription();
  #sortChangedSubscription: Subscription = new Subscription();
  #paginatorSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();
  #selectedQuelleSubscription: Subscription = new Subscription();

  isAdmin = false;
  storedQuelle: Quelle | undefined

  suchfilter!: Suchfilter;

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

  #selectedQuelle!: Quelle;
  #pageIndex = 0;
  #sortDirection: SortDirection = 'asc';

  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  constructor(public raetselFacade: RaetselFacade,
    public quellenFacade: QuellenFacade,
    private suchfilterFacade: SuchfilterFacade,
    private authFacade: AuthFacade,
    private changeDetector: ChangeDetectorRef
  ) {
    this.getScreenSize();
  }

  ngOnInit() {

    const _storedQuelle = localStorage.getItem(STORAGE_KEY_QUELLE);
    if (_storedQuelle) {
      this.storedQuelle = JSON.parse(_storedQuelle);
    }

    this.dataSource = new RaetselDataSource(this.raetselFacade);
    this.raetselFacade.checkOrLoadDeskriptoren();
    this.suchfilterFacade.setSuchfilter(this.raetselFacade.lastSuchfilter);

    this.#paginationStateSubscription = this.raetselFacade.paginationState$.subscribe(
      (state) => {
        this.anzahlRaetsel = state.anzahlTreffer;
        this.#pageIndex = state.pageIndex;
        this.#sortDirection = state.sortDirection === 'asc' ? 'asc' : 'desc';
      }
    );

    this.#canStartSucheSubscription = combineLatest([this.suchfilterFacade.selectedSuchfilter$, this.suchfilterFacade.suchfilterChanged$]).pipe(
      tap(([suchfilter, _changed]) => {
        if (suchfilter) {
          this.suchfilter = suchfilter;
        }
      }),
      filter(([suchfilter, changed]) => changed && suchkriterienVorhanden(suchfilter)),
      debounceTime(500),
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

    this.#selectedQuelleSubscription = this.quellenFacade.selectedQuelle$.subscribe(
      (quelle) => {
        if (quelle) {
          this.#selectedQuelle = quelle;
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
          // console.log(JSON.stringify(this.suchfilter));
          this.#triggerSuche();
        }
      })
    ).subscribe();

    this.changeDetector.detectChanges();
  }

  ngOnDestroy(): void {
    this.#suchfilterSubscription.unsubscribe();
    this.#userAdminSubscription.unsubscribe();
    this.#canStartSucheSubscription.unsubscribe();
    this.#sortChangedSubscription.unsubscribe();
    this.#paginatorSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
    this.#selectedQuelleSubscription.unsubscribe();
  }

  getDisplayedColumns(): string[] {

    if (this.isAdmin) {
      if (this.#scrWidth > 959) {
        return ['status', 'schluessel', 'name', 'kommentar'];
      } else {
        return ['status', 'schluessel', 'name'];
      }
    } else {
      if (this.#scrWidth > 959) {
        return ['schluessel', 'name', 'deskriptoren'];
      } else {
        return ['schluessel', 'name'];
      }
    }
  }

  getHeaderSchluessel(): string {

    if (this.#scrWidth > 959) {
      return 'SCHLUESSEL';
    } else {
      return 'SCHL';
    }
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
    this.raetselFacade.checkOrLoadDeskriptoren();
    this.suchfilterFacade.setSuchfilter({
      kontext: 'RAETSEL',
      deskriptoren: [],
      suchstring: ''
    });
  }

  neuesRaetsel(): void {
    this.raetselFacade.createAndEditRaetsel();
  }

  quelleWaehlen(): void {
    this.quellenFacade.navigateToQuellensuche();
  }

  canCreateRaetsel(): boolean {

    if (this.#selectedQuelle) {
      if (this.storedQuelle && this.storedQuelle.id === this.#selectedQuelle.id) {
        return true;
      }
      return this.#selectedQuelle.quellenart !== 'PERSON';
    }
    return this.storedQuelle !== undefined;
  }


  #triggerSuche(): void {

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ? this.paginator.pageIndex : this.#pageIndex,
      pageSize: this.paginator ? this.paginator.pageSize : 20,
      sortDirection: this.sort ? this.sort.direction : this.#sortDirection
    }

    this.raetselFacade.triggerSearch(this.suchfilter, pageDefinition);
  }

  #initPaginator(): void {

    this.paginator.pageIndex = this.#pageIndex;
    this.sort.direction = this.#sortDirection;
    // reset Paginator when sort changed
    this.#sortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  }
}
