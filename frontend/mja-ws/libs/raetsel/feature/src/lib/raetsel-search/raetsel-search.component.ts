import { AfterViewInit, ChangeDetectorRef, Component, HostListener, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, SortDirection } from '@angular/material/sort';
import { RaetselDataSource, RaetselFacade } from '@mja-ws/raetsel/api';
import { deskriptorenToString, Raetsel, RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { initialPageDefinition, PaginationState } from '@mja-ws/core/model';
import { merge, Subscription, tap } from 'rxjs';
import { AuthFacade } from '@mja-ws/shared/auth/api';

@Component({
  selector: 'mja-raetsel-search',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule
  ],
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss']
})
export class RaetselSearchComponent implements OnInit, OnDestroy, AfterViewInit {


  public authFacade = inject(AuthFacade);
  public raetselFacade = inject(RaetselFacade);
  public dataSource = inject(RaetselDataSource);


  isAdmin = false;
  anzahlRaetsel: number = 0;

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

  #userSubscription: Subscription = new Subscription();
  #matSortChangedSubscription: Subscription = new Subscription();
  #matPaginatorSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();


  #pageIndex = 0;
  #sortDirection: SortDirection = 'asc';

  constructor(private changeDetector: ChangeDetectorRef) { }

  ngOnInit(): void {

    this.#userSubscription = this.authFacade.user$.pipe(
      tap((user) => this.isAdmin = user.isAdmin)
    ).subscribe();

    this.#paginationStateSubscription = this.raetselFacade.paginationState$.subscribe(
      (state: PaginationState) => {
        this.anzahlRaetsel = state.anzahlTreffer;
        this.#pageIndex = state.pageDefinition.pageIndex;
        this.#sortDirection = state.pageDefinition.sortDirection === 'asc' ? 'asc' : 'desc';
      }
    );

  }

  ngOnDestroy(): void {

    this.#userSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
    this.#matPaginatorSubscription.unsubscribe();
    this.#matSortChangedSubscription.unsubscribe();
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

    this.#matPaginatorSubscription = merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => {
        // if (this.suchfilter !== undefined) {
        //   this.#triggerSuche();
        // }
      })
    ).subscribe();

    this.changeDetector.detectChanges();

    this.loadRaetsel();
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

  deskriptorenToString(raetsel: Raetsel): string {

    return deskriptorenToString(raetsel.deskriptoren);

  }

  onRowClicked(row: any): void {

    const raetsel: Raetsel = <Raetsel>row;
    // this.raetselFacade.selectRaetsel(raetsel);
  }

  loadRaetsel(): void {


    const suchfilter: RaetselSuchfilter = {
      deskriptoren: [],
      suchstring: 'zählen'
    };

    this.raetselFacade.triggerSearch(suchfilter, initialPageDefinition);

  }


  #initPaginator(): void {

    this.paginator.pageIndex = this.#pageIndex;
    this.sort.direction = this.#sortDirection;
    // reset Paginator when sort changed
    this.#matSortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  }
}
