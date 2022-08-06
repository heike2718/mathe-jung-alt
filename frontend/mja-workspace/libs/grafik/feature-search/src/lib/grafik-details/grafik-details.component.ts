import { Component, Input, OnInit } from '@angular/core';
import { GrafikFacade } from '@mja-workspace/grafik/domain';
import { UploadComponentModel } from '@mja-workspace/shared/ui-components';
import { GrafikSearchResult } from 'libs/grafik/domain/src/lib/entities/grafik.model';

@Component({
  selector: 'mja-grafik',
  templateUrl: './grafik-details.component.html',
  styleUrls: ['./grafik-details.component.scss'],
})
export class GrafikDetailsComponent implements OnInit {

  @Input()
  grafikSearchResult!: GrafikSearchResult;

  uploadModel!: UploadComponentModel;

  constructor(public grafikFacade: GrafikFacade) { }

  ngOnInit(): void {

    this.uploadModel = {
      typ: 'GRAFIK',
      pfad: this.grafikSearchResult.pfad,
      titel: 'Grafik hochladen',
      maxSizeBytes: 2097152,
      errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
      accept: '.eps',
      acceptMessage: 'erlaubte Dateitypen: eps'
    };

    console.log(JSON.stringify(this.uploadModel));
  }
}
