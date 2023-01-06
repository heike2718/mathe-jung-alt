import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Antwortvorschlag } from '@mja-ws/raetsel/model';

@Component({
  selector: 'mja-antwortvorschlag',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './antwortvorschlag.component.html',
  styleUrls: ['./antwortvorschlag.component.scss'],
})
export class AntwortvorschlagComponent {

  @Input()
  antwortvorschlag!: Antwortvorschlag;

  isKorrekt(): boolean {
    return this.antwortvorschlag.korrekt;    
  }
}
