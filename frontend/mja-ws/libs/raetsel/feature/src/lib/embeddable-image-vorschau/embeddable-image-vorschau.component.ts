import { Component, inject, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileInfoComponent, FileInfoModel, SelectFileComponent, SelectFileModel } from '@mja-ws/shared/components';
import { MatButtonModule } from '@angular/material/button';
import { GrafikFacade } from '@mja-ws/grafik/api';
import { EmbeddableImagesFacade } from '@mja-ws/embeddable-images/api';
import { Subscription } from 'rxjs';
import { EmbeddableImageVorschau } from '@mja-ws/embeddable-images/model';

@Component({
  selector: 'mja-embeddable-image-vorschau',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    SelectFileComponent,
    FileInfoComponent
  ],
  templateUrl: './embeddable-image-vorschau.component.html',
  styleUrls: ['./embeddable-image-vorschau.component.scss'],
})
export class EmbeddableImageVorschauComponent implements OnInit, OnDestroy {

  grafikFacade = inject(GrafikFacade);

  @Input()
  schluessel = '';

  @Input()
  raetselId = '';

  selectedEmbeddableImageVorschau: EmbeddableImageVorschau | undefined;

  fileInfo: FileInfoModel | undefined;

  pfadGrafik: string | undefined;

  selectFileModel: SelectFileModel = {
    maxSizeBytes: 2097152,
    errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
    accept: '.eps',
    acceptMessage: 'erlaubte Dateitypen: eps',
    titel: 'Grafikdatei hochladen',
    beschreibung: 'Die neue Datei ersetzt die aktuell importierte Grafik im LaTeX.'
  };


  #embeddableImagesFacade = inject(EmbeddableImagesFacade);

  #grafikSelectedSubscription: Subscription = new Subscription();
  #grafikHochgeladenSubscription: Subscription = new Subscription();

  ngOnInit(): void {

    this.#grafikSelectedSubscription = this.grafikFacade.selectedEmbeddableImageVorschau$.subscribe(
      (selectedEmbeddableImageVorschau) => this.selectedEmbeddableImageVorschau = selectedEmbeddableImageVorschau);

    this.#grafikHochgeladenSubscription = this.grafikFacade.grafikHochgeladenMessage$.subscribe(
      (message) => {
        if (message && message.level === 'INFO' && this.pfadGrafik) {
          this.grafikFacade.vorschauLaden(this.pfadGrafik);
        }
      }
    )
  }

  ngOnDestroy(): void {
    this.grafikFacade.clearVorschau();
    this.#grafikSelectedSubscription.unsubscribe();
    this.#grafikHochgeladenSubscription.unsubscribe();
  }

  onFileSelected($event: FileInfoModel): void {
    this.fileInfo = $event;
  }

  uploadFile(): void {
    if (this.selectedEmbeddableImageVorschau && this.fileInfo) {

      if (this.selectedEmbeddableImageVorschau.exists) {
        this.#embeddableImagesFacade.replaceEmbeddableImage(this.raetselId, this.selectedEmbeddableImageVorschau.pfad, this.fileInfo.file);
      } else {
        this.#embeddableImagesFacade.createEmbeddableImage({ raetselId: this.raetselId, textart: 'FRAGE' }, this.fileInfo.file);
      }
      this.fileInfo = undefined;
    }
  }

  reset(): void {
    this.grafikFacade.clearVorschau();
    this.fileInfo = undefined;
  }
}
