import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { Raetselgruppenelement, RaetselgruppenFacade } from '@mja-workspace/raetselgruppen/domain';
import { RaetselgruppenelementeDataSource } from './raetselgruppenelemente-datasource';

@Component({
  selector: 'mja-raetselgruppenelemente',
  templateUrl: './raetselgruppenelemente.component.html',
  styleUrls: ['./raetselgruppenelemente.component.scss']
})
export class RaetselgruppenelementeComponent implements AfterViewInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<Raetselgruppenelement>;
  dataSource: RaetselgruppenelementeDataSource;

  displayedColumns = ['schluessel', 'nummer', 'punkte', 'name'];

  constructor(private raetselgruppenFacade: RaetselgruppenFacade) {
    this.dataSource = new RaetselgruppenelementeDataSource(this.raetselgruppenFacade);
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }
}
