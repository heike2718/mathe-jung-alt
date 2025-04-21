import { Component, Input, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Configuration } from '@rbk-ws/shared/config';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { EmbeddableImagesFacade } from '@rbk-ws/embeddable-images/api';
import { EmbeddableImageInfo } from '@rbk-ws/embeddable-images/model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { InfoDialogComponent, InfoDialogModel } from '@rbk-ws/shared/components';
import { Subscription } from 'rxjs';

@Component({
    selector: 'rbk-embeddable-image-info',
    imports: [
        CommonModule,
        MatIconModule,
        MatButtonModule,
        MatDialogModule
    ],
    templateUrl: './embeddable-image-info.component.html',
    styleUrls: ['./embeddable-image-info.component.scss']
})
export class EmbeddableImageInfoComponent implements OnInit, OnDestroy {

  @Input()
  embeddableImageInfo!: EmbeddableImageInfo;

  #config = inject(Configuration);
  devMode = !this.#config.production;

  dialog = inject(MatDialog);

  #embeddableImagesFacade = inject(EmbeddableImagesFacade);

  #selectedInfoSubscription: Subscription = new Subscription();

  ngOnInit(): void {

    this.#selectedInfoSubscription = this.#embeddableImagesFacade.selectedEmbeddableImageInfo$.subscribe((info) => {
      if(info.existiert && info.pfad === this.embeddableImageInfo.pfad) {
        this.#embeddableImagesFacade.vorschauLaden(info);               
      }
    });
  }

  ngOnDestroy(): void {
    this.#selectedInfoSubscription.unsubscribe();
    this.#embeddableImagesFacade.clearVorschau();
  }

  expand() {

    this.#embeddableImagesFacade.handleEmbeddableImageInfoExpanded(this.embeddableImageInfo);

    if (!this.embeddableImageInfo.existiert) {

      const dialogData: InfoDialogModel = {
        ueberschrift: 'Grafikdatei nicht gefunden',
        text: 'Die Grafikdatei mit dem Pfad ' + this.embeddableImageInfo.pfad + ' existiert nicht. Bitte im Bearbeitungsmodus die Datei hochladen. '
          + 'Dabei werden vom System ein neuer Dateiname sowie der dazu passende \\includegraphics-Befehl generiert. Ersetzen Sie anschließend den '
          + 'ungültigen \\includegraphics-Befehll durch den neu generierten.'
      };

      this.#openInfoDialog(dialogData);
    }
  }

  #openInfoDialog(model: InfoDialogModel): void {

    const dialogRef = this.dialog.open(InfoDialogComponent, {
      height: '300px',
      width: '600px',
      data: model,
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(() => {
      // nothing
    });


  }
}
