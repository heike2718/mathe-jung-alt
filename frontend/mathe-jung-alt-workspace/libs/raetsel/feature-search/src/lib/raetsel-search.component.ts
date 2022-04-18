import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { RaetselDataSource, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { tap } from 'rxjs/operators';
import { merge, Subscription } from 'rxjs';

@Component({
  selector: 'mja-raetsel-search',
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss'],
})
export class RaetselSearchComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['schluessel', 'name', 'deskriptoren'];
  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  private raetselSubscription: Subscription = new Subscription();

  constructor(public raetselFacade: RaetselFacade) { }

  ngOnInit() {
    this.dataSource = new RaetselDataSource(this.raetselFacade);
    // this.raetselFacade.loadRaetsel();

    this.raetselSubscription = this.raetselFacade.raetselList$.subscribe(
      liste => this.anzahlRaetsel = liste.length
    );
  }

  onInputChanged($event: string) {
    if ($event.trim().length > 0) {
      this.paginator.pageIndex = 0;
      this.findRaetsel($event); 
    }      
  }

  ngAfterViewInit(): void {

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => this.loadRaetselPage())
    ).subscribe();
  }

  ngOnDestroy(): void {
      this.raetselSubscription.unsubscribe();
  }

  onRowClicked(row: any): void {
    console.log('row clicked: ' + JSON.stringify(row));
  }

  private findRaetsel(value: string): void {
    this.raetselFacade.findRaetsel(value);
  }

  private loadRaetselPage(): void {
    this.raetselFacade.slicePage(this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
