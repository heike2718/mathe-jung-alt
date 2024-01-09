import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AufgabensammlungenFacade } from '@mja-ws/aufgabensammlungen/api';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FrageLoesungImagesComponent, JaNeinDialogComponent, JaNeinDialogData, GeneratorParametersDialogAutorenComponent } from '@mja-ws/shared/components';
import {
  anzeigeAntwortvorschlaegeSelectInput, FontName, fontNamenSelectInput, GeneratedImages, LaTeXLayoutAntwortvorschlaege,
  Schriftgroesse,
  schriftgroessenSelectInput,
  SelectGeneratorParametersUIModelAutoren,
  User,
  verwendungszweckeAutorenSelectInput,
  verwendungszweckePublicSelectInput
} from '@mja-ws/core/model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { MatBadgeModule } from '@angular/material/badge';
import { Subscription } from 'rxjs';
import { EditAufgabensammlungselementPayload, Aufgabensammlungselement, AufgabensammlungDetails } from '@mja-ws/aufgabensammlungen/model';
import { AufgabensammlungselementDialogData } from '../aufgabensammlungselement-dialog/aufgabensammlungselement-dialog.data';
import { AufgabensammlungselementDialogComponent } from '../aufgabensammlungselement-dialog/aufgabensammlungenselement-dialog.component';
import { AufgabensammlungselementeComponent } from '../aufgabensammlungselement/aufgabensammlungselemente.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { FormsModule } from '@angular/forms';
import { AuthFacade } from '@mja-ws/core/api';

@Component({
  selector: 'mja-aufgabensammlungen-details',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatBadgeModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatGridListModule,
    MatInputModule,
    MatListModule,
    MatFormFieldModule,
    FrageLoesungImagesComponent,
    GeneratorParametersDialogAutorenComponent,
    AufgabensammlungselementDialogComponent,
    AufgabensammlungselementeComponent
  ],
  templateUrl: './aufgabensammlungen-details.component.html',
  styleUrls: ['./aufgabensammlungen-details.component.scss'],
})
export class AufgabensammlungDetailsComponent implements OnInit, OnDestroy {

  aufgabensammlungenFacade = inject(AufgabensammlungenFacade);
  
  user!: User;

  sammlung!: AufgabensammlungDetails;  

  selectedElement: Aufgabensammlungselement | undefined

  dialog = inject(MatDialog);

  images: GeneratedImages | undefined;

  // sind das model für die template driven form elements
  freigegeben = false;
  privat = true;
  schluessel = '';
  nummer = '';
  punkte = '';

  #raetselFacade = inject(RaetselFacade);
  #authFacade = inject(AuthFacade);

  

  #aufgabensammlungSubscription = new Subscription();
  #imagesSubscription = new Subscription();
  #aufgabensammlungselementSubscription = new Subscription();
  #userSubscription = new Subscription();

  ngOnInit(): void {

    this.#aufgabensammlungSubscription = this.aufgabensammlungenFacade.aufgabensammlungDetails$.subscribe(
      (aufgabensammlung) => {
        this.sammlung = aufgabensammlung;
        this.freigegeben = aufgabensammlung.freigegeben;
        this.privat = aufgabensammlung.privat;
      });

    this.#aufgabensammlungselementSubscription = this.aufgabensammlungenFacade.selectedAufgabensammlungselement$.subscribe(
      (element) => {
        this.selectedElement = element;
        if (element) {
          this.schluessel = element.raetselSchluessel;
          this.nummer = element.nummer;
          this.punkte = '' + element.punkte;
        } else {
          this.schluessel = '';
          this.nummer = '';
          this.punkte = '';
        }
      }
    );

    this.#userSubscription = this.#authFacade.user$.subscribe(user => this.user = user);

    this.#imagesSubscription = this.aufgabensammlungenFacade.selectedElementImages$.subscribe((images) => this.images = images);
  }

  ngOnDestroy(): void {
    this.#aufgabensammlungSubscription.unsubscribe();
    this.#imagesSubscription.unsubscribe();
    this.#aufgabensammlungselementSubscription.unsubscribe();
    this.#userSubscription.unsubscribe();
  }

  getAufgabensammlungID(): string {
    return this.sammlung ? this.sammlung.id : 'neu';
  }

  gotoUebersicht(): void {
    this.aufgabensammlungenFacade.unselectAufgabensammlung();
  }

  gotoRaetselDetails(): void {

    if (this.schluessel) {
      this.#raetselFacade.selectRaetsel(this.schluessel);
    }

  }

  openGenerateDialog(): void {

    if (this.getAufgabensammlungID().length === 0) {
      return;
    }

    const dialogData: SelectGeneratorParametersUIModelAutoren = {
      titel: 'File generieren',
      showVerwendungszwecke: true,
      verwendungszwecke: this.user.isAdmin ? verwendungszweckeAutorenSelectInput : verwendungszweckePublicSelectInput,
      selectedVerwendungszweck: undefined,
      layoutsAntwortvorschlaegeInput: anzeigeAntwortvorschlaegeSelectInput,
      selectedLayoutAntwortvorschlaege: undefined,
      fontNamen: fontNamenSelectInput,
      selectedFontName: undefined,
      schriftgroessen: schriftgroessenSelectInput,
      selectedSchriftgroesse: undefined,
    }

    const dialogRef = this.dialog.open(GeneratorParametersDialogAutorenComponent, {
      height: '750px',
      width: '700px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && dialogData.selectedVerwendungszweck) {

        let font: FontName = 'STANDARD';
        let size: Schriftgroesse = 'NORMAL';
        let layout: LaTeXLayoutAntwortvorschlaege = 'NOOP';

        if (dialogData.selectedLayoutAntwortvorschlaege) {
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
          case 'Arbeitsblatt': this.aufgabensammlungenFacade.generiereArbeitsblatt(this.getAufgabensammlungID(), font, size, layout); break;
          case 'Kartei': this.aufgabensammlungenFacade.generiereKnobelkartei(this.getAufgabensammlungID(), font, size, layout); break;
          case 'Vorschau': this.aufgabensammlungenFacade.generiereVorschau(this.getAufgabensammlungID(), font, size, layout); break;
          case 'LaTeX': this.aufgabensammlungenFacade.generiereLaTeX(this.getAufgabensammlungID(), font, size, layout); break;
        }
      }
    });
  }

  startEdit(): void {
    this.aufgabensammlungenFacade.editAufgabensammlung(this.sammlung);
  }

  reload(): void {
    this.aufgabensammlungenFacade.reloadAufgabensammlung(this.sammlung);
  }

  toggleStatus(): void {
    this.aufgabensammlungenFacade.toggleStatus(this.sammlung);
  }

  openNeuesAufgabensammlungselementDialog(): void {

    const dialogData: AufgabensammlungselementDialogData = {
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
    return this.sammlung.id === 'neu' || this.sammlung.elemente.length === 0;
  }

  onEditElement($element: Aufgabensammlungselement): void {

    const dialogData: AufgabensammlungselementDialogData = {
      titel: 'Element ändern',
      id: $element.id,
      modusAendern: true,
      nummer: $element.nummer,
      schluessel: $element.raetselSchluessel,
      punkte: $element.punkte
    };

    this.#initAndOpenEditElementDialog(dialogData);
  }

  onDeleteElement($element: Aufgabensammlungselement): void {
    this.#openConfirmLoeschenDialog($element);
  }

  onShowImagesElement($element: Aufgabensammlungselement): void {

    this.aufgabensammlungenFacade.selectAufgabensammlungselement($element);
  }

  #initAndOpenEditElementDialog(dialogData: AufgabensammlungselementDialogData): void {
    const dialogRef = this.dialog.open(AufgabensammlungselementDialogComponent, {
      height: '400px',
      width: '500px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && this.sammlung) {
        const data: AufgabensammlungselementDialogData = result as AufgabensammlungselementDialogData;
        this.#saveElement(data);
      }
    });
  }

  #saveElement(data: AufgabensammlungselementDialogData): void {

    const payload: EditAufgabensammlungselementPayload = {
      id: data.id,
      raetselSchluessel: data.schluessel,
      nummer: data.nummer,
      punkte: data.punkte
    };

    this.aufgabensammlungenFacade.saveAufgabensammlungselement(this.getAufgabensammlungID(), payload);

  }

  #openConfirmLoeschenDialog(element: Aufgabensammlungselement): void {

    const dialogData: JaNeinDialogData = {
      frage: 'Soll das Element der Aufgabensammlung wirklich gelöscht werden?',
      hinweis: 'weg ist weg'
    }

    const dialogRef = this.dialog.open(JaNeinDialogComponent, {
      height: '300px',
      width: '700px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.aufgabensammlungenFacade.deleteAufgabensammlungselement(this.getAufgabensammlungID(), element);
        this.images = undefined;
      }
    });
  }
}
