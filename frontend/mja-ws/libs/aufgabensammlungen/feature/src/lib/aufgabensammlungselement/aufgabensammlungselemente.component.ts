import { AfterViewInit, Component, EventEmitter, HostListener, inject, Input, Output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTable, MatTableModule } from '@angular/material/table';
import { AufgabensammlungDetails, Aufgabensammlungselement } from '@mja-ws/aufgabensammlungen/model';
import { AufgabensammlungselementeDataSource } from './aufgabensammlungselemente.datasource';
import { MatButtonModule } from '@angular/material/button';
import { MatBadgeModule } from '@angular/material/badge';
import { Benutzerart } from '@mja-ws/core/model';

@Component({
  selector: 'mja-aufgabensammlungselement',
  standalone: true,
  imports: [
    CommonModule,
    MatBadgeModule,
    MatButtonModule,
    MatTableModule
  ],
  templateUrl: './aufgabensammlungselemente.component.html',
  styleUrls: ['./aufgabensammlungselemente.component.scss'],
})
export class AufgabensammlungselementeComponent implements AfterViewInit {

  @Input()
  aufgabensammlung!: AufgabensammlungDetails;

  @Input()
  benutzerart!: Benutzerart;

  @ViewChild(MatTable)
  table!: MatTable<Aufgabensammlungselement>

  // Declare height and width variables
  #scrWidth!: number;

  @HostListener('window:resize', ['$event'])
  getScreenSize() {
    this.#scrWidth = window.innerWidth;
  }

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

  getHeaderSchluessel(): string {

    if (this.#scrWidth > 959) {
      return 'SCHLUESSEL';
    } else {
      return 'SCHL';
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
