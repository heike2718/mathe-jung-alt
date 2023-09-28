import { AfterViewInit, Component, EventEmitter, inject, Input, Output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTable, MatTableModule } from '@angular/material/table';
import { RaetselgruppeDetails, Raetselgruppenelement } from '@mja-ws/raetselgruppen/model';
import { RaetselgruppenelementeDataSource } from './raetselgruppenelemente.datasource';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'mja-raetselgruppenelemente',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatTableModule
  ],
  templateUrl: './raetselgruppenelemente.component.html',
  styleUrls: ['./raetselgruppenelemente.component.scss'],
})
export class RaetselgruppenelementeComponent implements AfterViewInit {
 
  @Input()
  raetselgruppe!: RaetselgruppeDetails;

  @ViewChild(MatTable)
  table!: MatTable<Raetselgruppenelement>

  dataSource = inject(RaetselgruppenelementeDataSource);  

  @Output()
  showImages: EventEmitter<Raetselgruppenelement> = new EventEmitter<Raetselgruppenelement>();

  @Output()
  editElement: EventEmitter<Raetselgruppenelement> = new EventEmitter<Raetselgruppenelement>();

  @Output()
  deleteElement: EventEmitter<Raetselgruppenelement> = new EventEmitter<Raetselgruppenelement>();

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
