import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Antwortvorschlag } from '@rbk-ws/raetsel/model';

@Component({
  selector: 'rbk-antwortvorschlag',
  imports: [CommonModule],
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
