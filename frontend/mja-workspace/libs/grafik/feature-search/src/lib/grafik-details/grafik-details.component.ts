import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { GrafikFacade } from '@mja-workspace/grafik/domain';
import { UploadComponentModel } from '@mja-workspace/shared/ui-components';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-grafik',
  templateUrl: './grafik-details.component.html',
  styleUrls: ['./grafik-details.component.scss'],
})
export class GrafikDetailsComponent implements OnInit, OnDestroy {

  uploadModel: UploadComponentModel = {
    typ: 'GRAFIK',
    pfad: '',
    titel: 'Grafik hochladen',
    maxSizeBytes: 2097152,
    errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
    accept: '.eps',
    acceptMessage: 'erlaubte Dateitypen: eps'
  };

  #selectedGrafikSubscription: Subscription = new Subscription();

  constructor(public grafikFacade: GrafikFacade) { }

  ngOnInit(): void {

    this.#selectedGrafikSubscription = this.grafikFacade.getSelectedGrafikSearchResult$.subscribe(
      (selectedGrafik) => {
        if (selectedGrafik && selectedGrafik.pfad.length > 0) {
          this.uploadModel = { ...this.uploadModel, pfad: selectedGrafik.pfad };
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.#selectedGrafikSubscription.unsubscribe();
  }
}
