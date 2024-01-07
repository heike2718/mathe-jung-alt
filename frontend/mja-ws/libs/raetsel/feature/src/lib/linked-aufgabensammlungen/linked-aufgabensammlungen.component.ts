import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LinkedAufgabensammlung } from '@mja-ws/raetsel/model';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'mja-linked-as',
  standalone: true,
  imports: [
    CommonModule,
    MatBadgeModule,
    MatButtonModule,
    MatCardModule
  ],
  templateUrl: './linked-aufgabensammlungen.component.html',
  styleUrl: './linked-aufgabensammlungen.component.scss',
})
export class LinkedAufgabensammlungenComponent {

  @Input()
  aufgabensammlung!: LinkedAufgabensammlung;

  @Output()
  detailsClicked: EventEmitter<LinkedAufgabensammlung> = new EventEmitter<LinkedAufgabensammlung>();

  showDetails(): void {
    this.detailsClicked.emit(this.aufgabensammlung);
  }

}
