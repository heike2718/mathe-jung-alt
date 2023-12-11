import { AfterViewInit, Component, EventEmitter, inject, Input, Output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTable, MatTableModule } from '@angular/material/table';
import { AufgabensammlungDetails, Aufgabensammlungselement } from '@mja-ws/aufgabensammlungen/model';
import { AufgabensammlungselementeDataSource } from './aufgabensammlungselemente.datasource';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'mja-aufgabensammlungselement',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatTableModule
  ],
  templateUrl: './aufgabensammlungselemente.component.html',
  styleUrls: ['./aufgabensammlungselemente.component.scss'],
})
export class AufgabensammlungselementeComponent implements AfterViewInit {
 
  @Input()
  aufgabensammlung!: AufgabensammlungDetails;

  @ViewChild(MatTable)
  table!: MatTable<Aufgabensammlungselement>

  dataSource = inject(AufgabensammlungselementeDataSource);  

  @Output()
  showImages: EventEmitter<Aufgabensammlungselement> = new EventEmitter<Aufgabensammlungselement>();

  @Output()
  editElement: EventEmitter<Aufgabensammlungselement> = new EventEmitter<Aufgabensammlungselement>();

  @Output()
  deleteElement: EventEmitter<Aufgabensammlungselement> = new EventEmitter<Aufgabensammlungselement>();

  ngAfterViewInit(): void {
    this.table.dataSource = this.dataSource;
  }

  getDisplayedColumns(): string[] {

    if (this.aufgabensammlung.schreibgeschuetzt) {
      return ['schluessel', 'nummer', 'punkte', 'name', 'loesungsbuchstabe', 'show'];

    } else {
      return ['schluessel', 'nummer', 'punkte', 'name', 'loesungsbuchstabe', 'show', 'edit', 'delete'];
    }
  }

  showImagesClicked(element: Aufgabensammlungselement): void {
    this.showImages.emit(element);
  }

  editElementClicked(element: Aufgabensammlungselement): void {
    this.editElement.emit(element);
  }

  deleteElementClicked(element: Aufgabensammlungselement): void {
    this.deleteElement.emit(element);
  }
}
