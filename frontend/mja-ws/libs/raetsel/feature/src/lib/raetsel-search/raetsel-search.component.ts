import { AfterViewInit, ChangeDetectorRef, Component, HostListener, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule, SortDirection } from '@angular/material/sort';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RaetselDataSource, RaetselFacade } from '@mja-ws/raetsel/api';
import { deskriptorenToString, initialRaetselSuchfilter, isSuchfilterEmpty, Raetsel, RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { initialSelectItemsComponentModel, PageDefinition, PaginationState, QuelleUI, SelectableItem, SelectItemsCompomentModel } from '@mja-ws/core/model';
import { combineLatest, debounceTime, merge, Subscription, tap } from 'rxjs';
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { CoreFacade } from '@mja-ws/core/api';
import { RaetselSuchfilterAdminComponent } from '../raetsel-suchfilter-admin/raetsel-suchfilter-admin.component';
import { SelectItemsComponent } from '@mja-ws/shared/components';

@Component({
  selector: 'mja-raetsel-search',
  standalone: true,
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatTableModule,
    MatButtonModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    RaetselSuchfilterAdminComponent,
    SelectItemsComponent
  ],
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss']
})
export class RaetselSearchComponent implements OnInit, OnDestroy, AfterViewInit {

  suchfilter: RaetselSuchfilter = initialRaetselSuchfilter;
  dataSource = inject(RaetselDataSource);

  coreFacade = inject(CoreFacade);

  isAdmin = false;
  anzahlRaetsel = 0;

  selectItemsCompomentModel: SelectItemsCompomentModel = initialSelectItemsComponentModel;

  #raetselFacade = inject(RaetselFacade);
  #authFacade = inject(AuthFacade);

  // Declare height and width variables
  #scrWidth!: number;

  @HostListener('window:resize', ['$event'])
  getScreenSize() {
    this.#scrWidth = window.innerWidth;
  }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  #userSubscription: Subscription = new Subscription();
  #matSortChangedSubscription: Subscription = new Subscription();
  #matPaginatorSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();
  #deskriptorenSubscription: Subscription = new Subscription();
  #suchfilterSubscription: Subscription = new Subscription();
  #quelleSubscription: Subscription = new Subscription();


  #pageIndex = 0;
  #sortDirection: SortDirection = 'asc';
  #quelle: QuelleUI | undefined;

  constructor(private changeDetector: ChangeDetectorRef) { }

  ngOnInit(): void {

    this.#userSubscription = this.#authFacade.user$.pipe(
      tap((user) => this.isAdmin = user.isAdmin)
    ).subscribe();

    this.#paginationStateSubscription = this.#raetselFacade.paginationState$.subscribe(
      (state: PaginationState) => {
        this.anzahlRaetsel = state.anzahlTreffer;
        this.#pageIndex = state.pageDefinition.pageIndex;
        this.#sortDirection = state.pageDefinition.sortDirection === 'asc' ? 'asc' : 'desc';
      }
    );

    this.#deskriptorenSubscription = combineLatest([this.#raetselFacade.suchfilter$, this.coreFacade.alleDeskriptoren$]).subscribe(

      ([selectedSuchfilter, alleDeskriptoren]) => {
        if (selectedSuchfilter) {
          this.selectItemsCompomentModel = this.#raetselFacade.initSelectItemsCompomentModel(selectedSuchfilter.deskriptoren, alleDeskriptoren);
        }
      }
    );

    this.#suchfilterSubscription = this.#raetselFacade.suchfilter$.pipe(

      tap((suchfilter) => this.suchfilter = suchfilter),
      debounceTime(500),
      tap(() => {
        if (this.paginator && this.sort) {
          this.#triggerSuche();
        }
      })
    ).subscribe();

    this.#quelleSubscription = this.coreFacade.quelleAdmin$.subscribe(
      (q) => {
        this.#quelle = q
      }
    );
  }

  ngOnDestroy(): void {

    this.#userSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
    this.#matPaginatorSubscription.unsubscribe();
    this.#matSortChangedSubscription.unsubscribe();
    this.#deskriptorenSubscription.unsubscribe();
    this.#suchfilterSubscription.unsubscribe();
    this.#quelleSubscription.unsubscribe();
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
        if (!isSuchfilterEmpty(this.suchfilter)) {
          this.#triggerSuche();
        }
      })
    ).subscribe();

    this.changeDetector.detectChanges();
  }

  // getDisplayedColumns(): string[] {

  //   if (this.isAdmin) {
  //     if (this.#scrWidth > 959) {
  //       return ['status', 'schluessel', 'name', 'kommentar'];
  //     } else {
  //       return ['status', 'schluessel', 'name', 'kommentar'];
  //     }
  //   } else {
  //     if (this.#scrWidth > 959) {
  //       return ['schluessel', 'name', 'deskriptoren'];
  //     } else {
  //       return ['schluessel', 'name'];
  //     }
  //   }
  // }

  getDisplayedColumns(): string[] {

    if (this.isAdmin) {
      return ['status', 'schluessel', 'name', 'kommentar'];
    } else {
      return ['schluessel', 'name', 'kommentar'];
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

  neueSuche(): void {
    this.#raetselFacade.neueRaetselsuche();
  }

  neuesRaetsel(): void {
    this.#raetselFacade.createAndEditRaetsel(this.#quelle);
  }

  buttonNeueSucheDisabled(): boolean {
    return isSuchfilterEmpty(this.suchfilter);
  }

  onRowClicked(row: Raetsel): void {

    const raetsel: Raetsel = <Raetsel>row;
    this.#raetselFacade.selectRaetsel(raetsel);
  }

  onSuchfilterSuchstringChanged(suchstring: string): void {
    if (suchstring.length >= 4) {
      this.#raetselFacade.changeSuchfilterWithDeskriptoren(this.suchfilter.deskriptoren, suchstring);
    }
  }

  onSelectItemsCompomentModelChanged(model: SelectItemsCompomentModel): void {

    if (model.gewaehlteItems.length > 0) {

      this.#raetselFacade.changeSuchfilterWithSelectableItems(model.gewaehlteItems, this.suchfilter.suchstring);
    }
  }


  #initPaginator(): void {

    this.paginator.pageIndex = this.#pageIndex;
    this.sort.direction = this.#sortDirection;
    // reset Paginator when sort changed
    this.#matSortChangedSubscription = this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
  }

  #triggerSuche(): void {

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ? this.paginator.pageIndex : this.#pageIndex,
      pageSize: this.paginator ? this.paginator.pageSize : 20,
      sortDirection: this.sort ? this.sort.direction : this.#sortDirection
    }

    this.#raetselFacade.triggerSearch(this.isAdmin, this.suchfilter, pageDefinition);
  }
}
