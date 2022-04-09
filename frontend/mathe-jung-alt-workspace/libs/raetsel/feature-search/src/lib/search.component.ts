import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { RaetselDataService, RaetselDataSource, SearchFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
import { fromEvent, merge } from 'rxjs';

@Component({
  selector: 'raetsel-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild('input') input!: ElementRef;

  // raetselList$ = this.searchFacade.raetselList$;

  displayedColumns: string[] = ['schluessel', 'name'];
  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  constructor(private searchFacade: SearchFacade, private raetselDataService: RaetselDataService) { }

  ngOnInit() {
    this.load();
    this.dataSource = new RaetselDataSource(this.raetselDataService);
    this.dataSource.loadRaetsel();
    this.anzahlRaetsel = this.raetselDataService.anzahlRaetsel();
  }

  ngAfterViewInit(): void {

    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;
          this.loadRaetselPage();          
        })
      )
      .subscribe();

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => this.loadRaetselPage())
    ).subscribe();
  }

  load(): void {
    this.searchFacade.load();
  }

  onRowClicked(row: any): void {
    console.log('row clicked: ' + JSON.stringify(row));
  }

  loadRaetselPage(): void {

    this.dataSource.loadRaetsel(this.input.nativeElement.value, this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
