import { Component, Input, OnInit } from '@angular/core';
import { Antwortvorschlag } from '@mja-workspace/raetsel/domain';

@Component({
  selector: 'mja-antwortvorschlag',
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
