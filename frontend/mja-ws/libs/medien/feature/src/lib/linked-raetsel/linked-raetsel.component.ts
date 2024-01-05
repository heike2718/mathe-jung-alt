import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import { LinkedRaetsel } from '@mja-ws/medien/model';
import { MatBadgeModule } from '@angular/material/badge';

@Component({
  selector: 'mja-linked-raetsel',
  standalone: true,
  imports: [
    CommonModule,
    MatBadgeModule,
    MatButtonModule,
    MatCardModule
  ],
  templateUrl: './linked-raetsel.component.html',
  styleUrl: './linked-raetsel.component.scss',
})
export class LinkedRaetselComponent {

  @Input()
  raetsel!: LinkedRaetsel;

  @Output()
  detailsClicked: EventEmitter<LinkedRaetsel> = new EventEmitter<LinkedRaetsel>();

  showDetails(): void {
    this.detailsClicked.emit(this.raetsel);   
  }

}
