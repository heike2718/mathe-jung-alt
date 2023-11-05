import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmbeddableImageInfo } from '@mja-ws/raetsel/model';
import { Configuration } from '@mja-ws/shared/config';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { EmbeddableImagesFacade } from '@mja-ws/embeddable-images/api';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { Message } from '@mja-ws/shared/messaging/api';

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
  grafikHochgeladen: EventEmitter<Message> = new EventEmitter<Message>();

  #config = inject(Configuration);
  devMode = !this.#config.production;

  #embeddableImagesFacade = inject(EmbeddableImagesFacade);

  grafikLaden(link: string): void {
    this.#embeddableImagesFacade.vorschauLaden(link);
  }

  onGrafikHochgeladen($event: Message): void {
    this.grafikHochgeladen.emit($event);
  }
}
