import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmbeddableImageInfo } from '@mja-ws/raetsel/model';
import { Configuration } from '@mja-ws/shared/config';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { EmbeddableImagesFacade } from '@mja-ws/embeddable-images/api';

@Component({
  selector: 'mja-ws-embeddable-image-info',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './embeddable-image-info.component.html',
  styleUrls: ['./embeddable-image-info.component.scss'],
})
export class EmbeddableImageInfoComponent {

  @Input()
  embeddableImageInfo!: EmbeddableImageInfo;

  @Output()
  pfadSelected: EventEmitter<string> = new EventEmitter<string>();

  #config = inject(Configuration);
  devMode = !this.#config.production;

  selected($event: string) {
    this.pfadSelected.emit($event);
  }
}
