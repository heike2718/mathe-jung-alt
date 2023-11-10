import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Configuration } from '@mja-ws/shared/config';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { EmbeddableImagesFacade } from '@mja-ws/embeddable-images/api';
import { EmbeddableImageInfo } from '@mja-ws/embeddable-images/model';

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

  #config = inject(Configuration);
  devMode = !this.#config.production;

  #embeddableImagesFacade = inject(EmbeddableImagesFacade);

  expand() {
     this.#embeddableImagesFacade.handleEmbeddableImageInfoExpanded(this.embeddableImageInfo);
  }
}
