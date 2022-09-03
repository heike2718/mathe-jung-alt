import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { GrafikFacade } from '@mja-workspace/grafik/domain';
import { UploadComponentModel } from '@mja-workspace/shared/ui-components';
import { Message } from '@mja-workspace/shared/util-mja';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-grafik',
  templateUrl: './grafik-details.component.html',
  styleUrls: ['./grafik-details.component.scss'],
})
export class GrafikDetailsComponent implements OnInit, OnDestroy {

  @Output()
  responsePayload: EventEmitter<Message> = new EventEmitter<Message>();

  uploadModel: UploadComponentModel = {
    typ: 'GRAFIK',
    pfad: '',
    titel: 'Grafik hochladen',
    maxSizeBytes: 2097152,
    errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
    accept: '.eps',
    acceptMessage: 'erlaubte Dateitypen: eps'
  };

  grafikSelected = false;

  #selectedGrafikSubscription: Subscription = new Subscription();

  constructor(public grafikFacade: GrafikFacade) { }

  ngOnInit(): void {

    this.#selectedGrafikSubscription = this.grafikFacade.getSelectedGrafikSearchResult$.subscribe(
      (selectedGrafik) => {
        if (selectedGrafik) {
          this.uploadModel = { ...this.uploadModel, pfad: selectedGrafik.pfad };
          this.grafikSelected = true;
        } else {
          this.grafikSelected = false;
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.#selectedGrafikSubscription.unsubscribe();
  }

  onDateiAusgewaehlt(_event: string): void {
    // interessiert uns hier nicht so sehr.
  }

  onResponse(messagePayload: Message | any): void {

    if (messagePayload) {
      const message = this.uploadModel.pfad + ': ' + messagePayload.message;
      const mp: Message = { ...messagePayload, message: message };
      this.grafikFacade.grafikHochgeladen(mp);
      this.responsePayload.emit({level: messagePayload.level, message: this.uploadModel.pfad});
    }
  }

  reset(): void {
    this.grafikFacade.clearVorschau();
  }
}
