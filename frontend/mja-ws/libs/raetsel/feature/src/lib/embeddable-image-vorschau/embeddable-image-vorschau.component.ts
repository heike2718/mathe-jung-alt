import { Component, inject, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileInfoComponent, FileInfoModel, SelectFileComponent, SelectFileModel } from '@mja-ws/shared/components';
import { MatButtonModule } from '@angular/material/button';
import { EmbeddableImagesFacade } from '@mja-ws/embeddable-images/api';
import { Subscription } from 'rxjs';
import { EmbeddableImageContext, EmbeddableImageVorschau, TEXTART } from '@mja-ws/embeddable-images/model';
import { Configuration } from '@mja-ws/shared/config';

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

  embeddableImagesFacade = inject(EmbeddableImagesFacade);

  @Input()
  schluessel = '';

  @Input()
  raetselId = '';

  hinweisNichtExistierendeGrafik = '';

  #config = inject(Configuration);
  devMode = !this.#config.production;

  #selectedEmbeddableImageVorschau: EmbeddableImageVorschau | undefined;

  fileInfo: FileInfoModel | undefined;

  pfadGrafik: string | undefined;

  selectFileModel: SelectFileModel = {
    maxSizeBytes: 2097152,
    errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
    accept: '.eps',
    acceptMessage: 'erlaubte Dateitypen: eps',
    titel: 'Grafikdatei hochladen',
    beschreibung: 'Die neue Datei ersetzt die aktuell importierte Grafik im LaTeX.',
    hinweis: undefined
  };

  #selectedEmbeddableImageSubscription: Subscription = new Subscription();
  #embeddableImageResponseSubscription: Subscription = new Subscription();

  ngOnInit(): void {

    this.#selectedEmbeddableImageSubscription = this.embeddableImagesFacade.selectedEmbeddableImageVorschau$.subscribe(
      (selectedEmbeddableImageVorschau) => this.#selectedEmbeddableImageVorschau = selectedEmbeddableImageVorschau);

    this.#embeddableImageResponseSubscription = this.embeddableImagesFacade.embeddableImageResponse$.subscribe(
      (response) => {
        if (response.pfad !== '') {
          this.embeddableImagesFacade.vorschauLaden(response.pfad);
        }
      }
    )

    this.hinweisNichtExistierendeGrafik = 'Falls der Pfad stimmt, wurde die Datei noch nicht hochgeladen. Zum Hochladen bitte Rätsel bearbeiten.';
  }

  ngOnDestroy(): void {
    this.embeddableImagesFacade.clearVorschau();
    this.#selectedEmbeddableImageSubscription.unsubscribe();
    this.#embeddableImageResponseSubscription.unsubscribe();
  }

  showSelectFileComponent(): boolean {
    
    if (this.fileInfo) {
      return false;
    }

    return this.#selectedEmbeddableImageVorschau ? this.#selectedEmbeddableImageVorschau.exists : false;
  }

  onFileSelected($event: FileInfoModel): void {
    this.fileInfo = $event;
  }

  uploadFile(): void {
    if (this.fileInfo) {

      const context: EmbeddableImageContext = { raetselId: this.raetselId, textart: 'FRAGE' };

      if (this.#selectedEmbeddableImageVorschau) {
        this.embeddableImagesFacade.replaceEmbeddableImage(this.#selectedEmbeddableImageVorschau.pfad, context, this.fileInfo.file);
      }
      this.fileInfo = undefined;
    }
  }

  reset(): void {
    this.embeddableImagesFacade.clearVorschau();
    this.fileInfo = undefined;
  }
}
