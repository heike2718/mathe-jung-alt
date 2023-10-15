import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselgruppenFacade } from '@mja-ws/raetselgruppen/api';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FrageLoesungImagesComponent, JaNeinDialogComponent, JaNeinDialogData, GeneratorParametersDialogAutorenComponent } from '@mja-ws/shared/components';
import {
  anzeigeAntwortvorschlaegeSelectInput, FONT_NAME, fontNamenSelectInput, GeneratedImages, LATEX_LAYOUT_ANTWORTVORSCHLAEGE,
  SCHRIFTGROESSE,
  schriftgroessenSelectInput,
  SelectGeneratorParametersUIModelAutoren,
  verwendungszweckeAutorenSelectInput
} from '@mja-ws/core/model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { Subscription, tap } from 'rxjs';
import { EditRaetselgruppenelementPayload, RaetselgruppeBasisdaten, Raetselgruppenelement } from '@mja-ws/raetselgruppen/model';
import { RaetselgruppenelementDialogData } from '../raetselgruppenelement-dialog/raetselgruppenelement-dialog.data';
import { RaetselgruppenelementDialogComponent } from '../raetselgruppenelement-dialog/raetselgruppenelement-dialog.component';
import { RaetselgruppenelementeComponent } from '../raetselgruppenelemente/raetselgruppenelemente.component';
import { CoreFacade } from '@mja-ws/core/api';
import { RaetselgruppeEditComponent } from '../raetselgruppe-edit/raetselgruppe-edit.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'mja-raetselgruppen-details',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatDialogModule,
    MatGridListModule,
    MatInputModule,
    MatListModule,
    MatFormFieldModule,
    FrageLoesungImagesComponent,
    GeneratorParametersDialogAutorenComponent,
    RaetselgruppenelementDialogComponent,
    RaetselgruppenelementeComponent,
    RaetselgruppeEditComponent
  ],
  templateUrl: './raetselgruppen-details.component.html',
  styleUrls: ['./raetselgruppen-details.component.scss'],
})
export class RaetselgruppenDetailsComponent implements OnInit, OnDestroy {

  raetselgruppenFacade = inject(RaetselgruppenFacade);
  dialog = inject(MatDialog);

  images: GeneratedImages | undefined;
  schluessel = '';
  nummer = '';
  punkte = '';

  #coreFacade = inject(CoreFacade);

  #raetselgruppeSubscription = new Subscription();
  #imagesSubscription = new Subscription();

  #raetselgruppeBasisdaten!: RaetselgruppeBasisdaten;
  #anzahlElemente = 0;

  ngOnInit(): void {
    this.#raetselgruppeSubscription = this.raetselgruppenFacade.raetselgruppeDetails$.subscribe((raetselgruppe) => {
      this.#raetselgruppeBasisdaten = raetselgruppe;
      this.#anzahlElemente = raetselgruppe.elemente.length
    });
  }

  ngOnDestroy(): void {
    this.#raetselgruppeSubscription.unsubscribe();
    this.#imagesSubscription.unsubscribe();
  }

  getRaetselgruppeID(): string {
    return this.#raetselgruppeBasisdaten.id;
  }

  gotoUebersicht(): void {
    this.raetselgruppenFacade.unselectRaetselgruppe();
  }

  openGenerateDialog(): void {

    if (this.getRaetselgruppeID().length === 0) {
      return;
    }

    const dialogData: SelectGeneratorParametersUIModelAutoren = {
      titel: 'File generieren',
      showVerwendungszwecke: true,
      verwendungszwecke: verwendungszweckeAutorenSelectInput,
      selectedVerwendungszweck: undefined,
      layoutsAntwortvorschlaegeInput: anzeigeAntwortvorschlaegeSelectInput,
      selectedLayoutAntwortvorschlaege: undefined,
      fontNamen: fontNamenSelectInput,
      selectedFontName: undefined,
      schriftgroessen: schriftgroessenSelectInput,
      selectedSchriftgroesse: undefined
    }

    const dialogRef = this.dialog.open(GeneratorParametersDialogAutorenComponent, {
      height: '600px',
      width: '700px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && dialogData.selectedVerwendungszweck) {

        let font: FONT_NAME = 'STANDARD';
        let size: SCHRIFTGROESSE = 'NORMAL';
        let layout: LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'NOOP';

        if (dialogData.selectedFontName) {
          switch (dialogData.selectedLayoutAntwortvorschlaege) {
            case 'Ankreuztabelle': layout = 'ANKREUZTABELLE'; break;
            case 'Buchstaben': layout = 'BUCHSTABEN'; break;
            case 'Liste': layout = 'DESCRIPTION'; break;
          }
        }

        if (dialogData.selectedSchriftgroesse) {
          switch (dialogData.selectedSchriftgroesse) {
            case 'groß': size = 'LARGE'; break;
            case 'sehr groß': size = 'HUGE'; break;
          }
        }

        if (dialogData.selectedFontName) {
          switch (dialogData.selectedFontName) {
            case 'Druckschrift (Leseanfänger)': font = 'DRUCK_BY_WOK'; break;
            case 'Fibel Nord': font = 'FIBEL_NORD'; break;
            case 'Fibel Süd': font = 'FIBEL_SUED'; break;
          }
        }        

        switch (dialogData.selectedVerwendungszweck) {
          case 'Arbeitsblatt': this.raetselgruppenFacade.generiereArbeitsblatt(this.getRaetselgruppeID(), font, size); break;
          case 'Knobelkartei': this.raetselgruppenFacade.generiereKnobelkartei(this.getRaetselgruppeID(), font, size); break;
          case 'Vorschau': this.raetselgruppenFacade.generiereVorschau(this.getRaetselgruppeID(), font, size, layout); break;
          case 'LaTeX': this.raetselgruppenFacade.generiereLaTeX(this.getRaetselgruppeID(), layout); break;
        }
      }
    });
  }

  startEdit(): void {
    this.raetselgruppenFacade.editRaetselgruppe(this.#raetselgruppeBasisdaten);
  }

  reloadDisabled(): boolean {
    return !this.#basisdatenLoaded();
  }

  reload(): void {
    this.raetselgruppenFacade.reloadRaetselgruppe(this.#raetselgruppeBasisdaten, this.#anzahlElemente);
  }

  toggleStatusDisabled(): boolean {
    return !this.#basisdatenLoaded();
  }

  toggleStatus(): void {
    this.raetselgruppenFacade.toggleStatus(this.#raetselgruppeBasisdaten);
  }

  openNeuesRaetselgruppenelementDialog(): void {

    const dialogData: RaetselgruppenelementDialogData = {
      titel: 'Neues Element',
      id: 'neu',
      modusAendern: false,
      nummer: '',
      schluessel: '',
      punkte: 0
    };

    this.#initAndOpenEditElementDialog(dialogData);
  }

  buttonsGenerierenDisabled(): boolean {
    return this.getRaetselgruppeID().length === 0 || this.#anzahlElemente === 0;
  }

  onEditElement($element: Raetselgruppenelement): void {

    const dialogData: RaetselgruppenelementDialogData = {
      titel: 'Element ändern',
      id: $element.id,
      modusAendern: true,
      nummer: $element.nummer,
      schluessel: $element.raetselSchluessel,
      punkte: $element.punkte
    };

    this.#initAndOpenEditElementDialog(dialogData);
  }

  onDeleteElement($element: Raetselgruppenelement): void {
    if (this.#raetselgruppeBasisdaten) {
      this.#openConfirmLoeschenDialog($element);
    }
  }

  onShowImagesElement($element: Raetselgruppenelement): void {

    this.schluessel = $element.raetselSchluessel;
    this.nummer = $element.nummer;
    this.punkte = '' + $element.punkte;

    this.#imagesSubscription.unsubscribe();
    this.#imagesSubscription = this.#coreFacade.loadRaetselPNGs($element.raetselSchluessel).pipe(
      tap((images: GeneratedImages) => this.images = images)
    ).subscribe();

  }

  #initAndOpenEditElementDialog(dialogData: RaetselgruppenelementDialogData): void {
    const dialogRef = this.dialog.open(RaetselgruppenelementDialogComponent, {
      height: '400px',
      width: '500px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && this.#raetselgruppeBasisdaten) {
        const data: RaetselgruppenelementDialogData = result as RaetselgruppenelementDialogData;
        this.#saveReaetselgruppeElement(data);
      }
    });
  }

  #saveReaetselgruppeElement(data: RaetselgruppenelementDialogData): void {

    const payload: EditRaetselgruppenelementPayload = {
      id: data.id,
      raetselSchluessel: data.schluessel,
      nummer: data.nummer,
      punkte: data.punkte
    };

    this.raetselgruppenFacade.saveRaetselgruppenelement(this.getRaetselgruppeID(), payload);

  }

  #openConfirmLoeschenDialog(element: Raetselgruppenelement): void {

    const dialogData: JaNeinDialogData = {
      frage: 'Soll das Rätselgruppenelement wirklich gelöscht werden?',
      hinweis: 'weg ist weg'
    }

    const dialogRef = this.dialog.open(JaNeinDialogComponent, {
      height: '300px',
      width: '700px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.raetselgruppenFacade.deleteRaetselgruppenelement(this.getRaetselgruppeID(), element);
        this.images = undefined;
      }
    });
  }

  #basisdatenLoaded(): boolean {

    if (this.#raetselgruppeBasisdaten) {
      return true;
    }

    return false;
  }


}
