import { AfterViewInit, Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
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
export class RaetselgruppenelementeComponent implements AfterViewInit, OnInit {

  @ViewChild(MatTable) table!: MatTable<Raetselgruppenelement>;
  dataSource!: RaetselgruppenelementeDataSource;

  @Output()
  editElement: EventEmitter<Raetselgruppenelement> = new EventEmitter<Raetselgruppenelement>();

  @Output()
  deleteElement: EventEmitter<Raetselgruppenelement> = new EventEmitter<Raetselgruppenelement>();

  displayedColumns = ['schluessel', 'nummer', 'punkte', 'name', 'edit', 'delete'];

  constructor(private raetselgruppenFacade: RaetselgruppenFacade) { }

  ngOnInit(): void {
    this.dataSource = new RaetselgruppenelementeDataSource(this.raetselgruppenFacade);
  }

  ngAfterViewInit(): void {
    this.table.dataSource = this.dataSource;
  }

  editElementClicked(element: Raetselgruppenelement): void {
    this.editElement.emit(element);
  }

  deleteElementClicked(element: Raetselgruppenelement): void {
    this.deleteElement.emit(element);
  }
}
