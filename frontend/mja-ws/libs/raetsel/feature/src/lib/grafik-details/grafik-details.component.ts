import { Component, EventEmitter, inject, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Message } from '@mja-ws/shared/messaging/api';
import { FileUploadComponent, UploadComponentModel } from '@mja-ws/shared/components';
import { MatButtonModule } from '@angular/material/button';
import { Subscription } from 'rxjs';
import { GrafikFacade } from '@mja-ws/grafik/api';

@Component({
  selector: 'mja-grafik',
  standalone: true,
  imports: [
    CommonModule,
    FileUploadComponent,
    MatButtonModule
  ],
  templateUrl: './grafik-details.component.html',
  styleUrls: ['./grafik-details.component.scss'],
})
export class GrafikDetailsComponent implements OnInit, OnDestroy {

  grafikFacade = inject(GrafikFacade);

  @Input()
  schluessel = '';

  @Output()
  responsePayload: EventEmitter<Message> = new EventEmitter<Message>();

  uploadModel: UploadComponentModel = {
    schluesselRaetsel: '',
    pfad: '',
    titel: 'Grafik hochladen',
    maxSizeBytes: 2097152,
    errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
    accept: '.eps',
    acceptMessage: 'erlaubte Dateitypen: eps'
  };

  grafikSelected = false;

  #grafikFacade = inject(GrafikFacade);
  #selectedGrafikSubscription: Subscription = new Subscription();

  ngOnInit(): void {

    this.#selectedGrafikSubscription = this.#grafikFacade.grafikSearchResult$.subscribe(
      (selectedGrafik) => {
        if (selectedGrafik) {
          this.uploadModel = { ...this.uploadModel, pfad: selectedGrafik.pfad, schluesselRaetsel: this.schluessel };
          this.grafikSelected = true;
        }
      });
  }

  ngOnDestroy(): void {
    this.#grafikFacade.clearVorschau();
    this.#selectedGrafikSubscription.unsubscribe();
  }

  onDateiAusgewaehlt(_event: string): void {
    // interessiert uns hier nicht so sehr.
  }

  onResponse(messagePayload: Message | any): void {

    if (messagePayload) {
      const message = this.uploadModel.pfad + ': ' + messagePayload.message;
      const mp: Message = { ...messagePayload, message: message };

      if (mp.level === 'INFO') {
        this.#grafikFacade.grafikHochgeladen(mp);
        this.#grafikFacade.grafikPruefen(this.uploadModel.pfad);
      }


      this.#grafikFacade.grafikHochgeladen(mp);
      this.#grafikFacade.grafikPruefen(this.uploadModel.pfad);
      this.responsePayload.emit({ level: messagePayload.level, message: this.uploadModel.pfad });
    }
  }

  reset(): void {
    this.grafikSelected = false;
    this.uploadModel = {...this.uploadModel, pfad: ''};
    this.#grafikFacade.clearVorschau();
  }
}
