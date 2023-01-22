import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselgruppenFacade } from '@mja-ws/raetselgruppen/api';
import { MatButtonModule } from '@angular/material/button';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FrageLoesungImagesComponent, JaNeinDialogComponent, JaNeinDialogData, SelectPrintparametersDialogComponent } from '@mja-ws/shared/components';
import { anzeigeAntwortvorschlaegeSelectInput, GeneratedImages, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, SelectPrintparametersDialogData } from '@mja-ws/core/model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { Subscription, tap } from 'rxjs';
import { EditRaetselgruppenelementPayload, RaetselgruppeBasisdaten, Raetselgruppenelement } from '@mja-ws/raetselgruppen/model';
import { RaetselgruppenelementDialogData } from '../raetselgruppenelement-dialog/raetselgruppenelement-dialog.data';
import { RaetselgruppenelementDialogComponent } from '../raetselgruppenelement-dialog/raetselgruppenelement-dialog.component';
import { RaetselgruppenelementeComponent } from '../raetselgruppenelemente/raetselgruppenelemente.component';
import { CoreFacade } from '@mja-ws/core/api';
import { RaetselgruppeEditComponent } from '../raetselgruppe-edit/raetselgruppe-edit.component';

@Component({
  selector: 'mja-raetselgruppen-details',
  standalone: true,
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    MatListModule,
    MatFormFieldModule,    
    FrageLoesungImagesComponent,
    SelectPrintparametersDialogComponent,
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

  images?: GeneratedImages;
  schluessel = '';

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

  openPrintVorschauDialog(): void {

    if (this.getRaetselgruppeID().length === 0) {
      return;
    }

    const dialogData: SelectPrintparametersDialogData = {
      titel: 'Vorschau generieren',
      layoutsAntwortvorschlaegeInput: anzeigeAntwortvorschlaegeSelectInput,
      selectedLayoutAntwortvorschlaege: undefined
    }

    const dialogRef = this.dialog.open(SelectPrintparametersDialogComponent, {
      height: '300px',
      width: '700px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && dialogData.selectedLayoutAntwortvorschlaege) {

        let layout: LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'NOOP';
        switch (dialogData.selectedLayoutAntwortvorschlaege) {
          case 'ANKREUZTABELLE': layout = 'ANKREUZTABELLE'; break;
          case 'BUCHSTABEN': layout = 'BUCHSTABEN'; break;
          case 'DESCRIPTION': layout = 'DESCRIPTION'; break;
        }

        this.raetselgruppenFacade.generiereVorschau(this.getRaetselgruppeID(), layout);
      }
    });
  }

  generiereLaTeX(): void {
    this.raetselgruppenFacade.generiereLaTeX(this.getRaetselgruppeID());
  }

  startEdit(): void {
    this.raetselgruppenFacade.editRaetselgruppe(this.#raetselgruppeBasisdaten);
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
      }
    });
  }

  onShowImagesElement($element: Raetselgruppenelement): void {

    this.schluessel = $element.raetselSchluessel;

    this.#imagesSubscription.unsubscribe();
    this.#imagesSubscription = this.#coreFacade.loadRaetselPNGs($element.raetselSchluessel).pipe(
      tap((images: GeneratedImages) => this.images = images)
    ).subscribe();

  }
}
