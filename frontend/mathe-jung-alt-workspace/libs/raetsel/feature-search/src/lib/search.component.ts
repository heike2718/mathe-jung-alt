import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { RaetselDataSource, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
import { fromEvent, merge, Subscription } from 'rxjs';

@Component({
  selector: 'raetsel-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild('input') input!: ElementRef;

  displayedColumns: string[] = ['schluessel', 'name'];
  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  private raetselSubscription: Subscription = new Subscription();
  private keySubscription: Subscription = new Subscription();

  constructor(public raetselFacade: RaetselFacade) { }

  ngOnInit() {
    this.dataSource = new RaetselDataSource(this.raetselFacade);
    // this.raetselFacade.loadRaetsel();

    this.raetselSubscription = this.raetselFacade.raetselList$.subscribe(
      liste => this.anzahlRaetsel = liste.length
    );
  }

  ngAfterViewInit(): void {

    this.keySubscription = fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;
          this.findRaetsel();          
        })
      )
      .subscribe();

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => this.loadRaetselPage())
    ).subscribe();
  }

  ngOnDestroy(): void {
      this.raetselSubscription.unsubscribe();
      this.keySubscription.unsubscribe();
  }

  onRowClicked(row: any): void {
    console.log('row clicked: ' + JSON.stringify(row));
  }

  private findRaetsel(): void {
    this.raetselFacade.findRaetsel(this.input.nativeElement.value);
  }

  private loadRaetselPage(): void {
    this.raetselFacade.slicePage(this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
