import { AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { RaetselgruppeDetails, Raetselgruppenelement, RaetselgruppenFacade } from '@mja-workspace/raetselgruppen/domain';
import { RaetselgruppenelementeDataSource } from './raetselgruppenelemente.datasource';

@Component({
  selector: 'mja-raetselgruppenelemente',
  templateUrl: './raetselgruppenelemente.component.html',
  styleUrls: ['./raetselgruppenelemente.component.scss']
})
export class RaetselgruppenelementeComponent implements AfterViewInit, OnInit {

  @Input()
  raetselgruppe!: RaetselgruppeDetails;

  @ViewChild(MatTable) table!: MatTable<Raetselgruppenelement>;
  dataSource!: RaetselgruppenelementeDataSource;

  @Output()
  showImages: EventEmitter<Raetselgruppenelement> = new EventEmitter<Raetselgruppenelement>();

  @Output()
  editElement: EventEmitter<Raetselgruppenelement> = new EventEmitter<Raetselgruppenelement>();

  @Output()
  deleteElement: EventEmitter<Raetselgruppenelement> = new EventEmitter<Raetselgruppenelement>();

  constructor(private raetselgruppenFacade: RaetselgruppenFacade) { }

  ngOnInit(): void {
    this.dataSource = new RaetselgruppenelementeDataSource(this.raetselgruppenFacade);
  }

  ngAfterViewInit(): void {
    this.table.dataSource = this.dataSource;
  }

  getDisplayedColumns(): string[] {

    if (this.raetselgruppe.schreibgeschuetzt) {
      return ['schluessel', 'nummer', 'punkte', 'name', 'loesungsbuchstabe', 'show'];

    } else {
      return ['schluessel', 'nummer', 'punkte', 'name', 'loesungsbuchstabe', 'show', 'edit', 'delete'];
    }
  }

  showImagesClicked(element: Raetselgruppenelement): void {
    this.showImages.emit(element);
  }

  editElementClicked(element: Raetselgruppenelement): void {
    this.editElement.emit(element);
  }

  deleteElementClicked(element: Raetselgruppenelement): void {
    this.deleteElement.emit(element);
  }
}
