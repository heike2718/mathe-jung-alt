import { AfterViewInit, ChangeDetectorRef, Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { Raetselgruppenelement, RaetselgruppenFacade } from '@mja-workspace/raetselgruppen/domain';
import { Subscription, tap } from 'rxjs';
import { RaetselgruppenelementeDataSource } from './raetselgruppenelemente.datasource';

@Component({
  selector: 'mja-raetselgruppenelemente',
  templateUrl: './raetselgruppenelemente.component.html',
  styleUrls: ['./raetselgruppenelemente.component.scss']
})
export class RaetselgruppenelementeComponent implements AfterViewInit, OnInit, OnDestroy {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<Raetselgruppenelement>;
  dataSource!: RaetselgruppenelementeDataSource;

  #elementeSubscription = new Subscription();

  displayedColumns = ['schluessel', 'nummer', 'punkte', 'name'];

  constructor(private raetselgruppenFacade: RaetselgruppenFacade, private changeDetector: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.#elementeSubscription = this.raetselgruppenFacade.raetselgruppenelemente$.pipe(
      tap((elemente) => {
        if (this.dataSource) {
          this.dataSource.data = elemente;
        } else {
          this.dataSource = new RaetselgruppenelementeDataSource(elemente);
        }
      })
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.#elementeSubscription.unsubscribe();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;

    this.changeDetector.detectChanges();
  }
}
