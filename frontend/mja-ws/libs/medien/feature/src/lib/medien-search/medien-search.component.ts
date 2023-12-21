import { AfterViewInit, ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MedienDataSource, MedienFacade } from '@mja-ws/medien/api';
import { PageDefinition, PaginationState, initialPaginationState } from '@mja-ws/core/model';
import { Subscription, of, tap } from 'rxjs';
import { MediensucheTrefferItem } from '@mja-ws/medien/model';

@Component({
  selector: 'mja-medien-search',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatPaginatorModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  templateUrl: './medien-search.component.html',
  styleUrl: './medien-search.component.scss',
})
export class MedienSearchComponent implements OnInit, AfterViewInit, OnDestroy {

  dataSource = inject(MedienDataSource);
  anzahlMedien: number = 0;
  suchstring = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  #medienFacade = inject(MedienFacade);

  #paginationState: PaginationState = initialPaginationState;
  #pageIndex = 0;
  #adjusting = false;

  // Declare height and width variables
  #scrWidth = window.innerWidth;

  #matPaginatorSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();

  constructor(private changeDetector: ChangeDetectorRef) {
  }

  @HostListener('window:resize', ['$event'])
  getScreenSize() {
    // this.#scrHeight = window.innerHeight;
    this.#scrWidth = window.innerWidth;
  }


  ngOnInit(): void {

    this.#paginationStateSubscription = this.#medienFacade.paginationState$.subscribe(
      (state: PaginationState) => {
        this.anzahlMedien = state.anzahlTreffer;
        this.#pageIndex = state.pageDefinition.pageIndex;
      }
    );

    this.#triggerSearch();
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

    this.#matPaginatorSubscription = of(this.paginator.page).pipe(
      tap(() => {
        this.#paginationState = { ...this.#paginationState, pageDefinition: { ...this.#paginationState.pageDefinition, sortDirection: 'asc' } };
        this.#triggerSearch();
      })
    ).subscribe();

    // oder explizit nochmal changeDetection triggern
    this.changeDetector.detectChanges();
  }

  ngOnDestroy(): void {

    this.#matPaginatorSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
  }

  resetFilter(): void {

  }

  neuesMedium(): void {

    this.#medienFacade.createAndEditMedium();

  }

  getDisplayedColumns(): string[] {

    return ['medienart', 'titel', 'kommentar'];
  }

  onRowClicked(medium: MediensucheTrefferItem): void {

    // const medium: Raetsel = <Raetsel>row;
    this.#medienFacade.selectMedium(medium);
  }

  #initPaginator(): void {

    this.paginator.pageIndex = this.#pageIndex;
  }


  #triggerSearch(): void {

    if (this.#adjusting) {
      return;
    }

    const pageDefinition: PageDefinition = {
      pageIndex: this.paginator ? this.paginator.pageIndex : this.#pageIndex,
      pageSize: this.paginator ? this.paginator.pageSize : 20,
      sortDirection: 'asc'
    }

    this.#medienFacade.triggerSearch(this.suchstring, pageDefinition);
  }
}
