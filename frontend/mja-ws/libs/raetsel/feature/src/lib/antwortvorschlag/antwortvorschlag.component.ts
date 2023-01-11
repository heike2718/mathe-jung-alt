import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { Antwortvorschlag } from '@mja-ws/raetsel/model';

@Component({
  selector: 'mja-antwortvorschlag',
  standalone: true,
  imports: [
    CommonModule,
    FlexLayoutModule
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
