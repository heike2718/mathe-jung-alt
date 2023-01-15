import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselgruppenFacade } from '@mja-ws/raetselgruppen/api';
import { MatButtonModule } from '@angular/material/button';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { SelectPrintparametersDialogComponent } from '@mja-ws/shared/components';
import { anzeigeAntwortvorschlaegeSelectInput, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, SelectPrintparametersDialogData } from '@mja-ws/core/model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { Subscription } from 'rxjs';

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
    SelectPrintparametersDialogComponent
  ],
  templateUrl: './raetselgruppen-details.component.html',
  styleUrls: ['./raetselgruppen-details.component.scss'],
})
export class RaetselgruppenDetailsComponent implements OnInit, OnDestroy {

  raetselgruppenFacade = inject(RaetselgruppenFacade);
  dialog = inject(MatDialog);

  #raetselgruppeSubscription = new Subscription();
  #raetselgruppeID: string = '';
  #anzahlElemente = 0;

  constructor() {}

  ngOnInit(): void {
    this.#raetselgruppeSubscription = this.raetselgruppenFacade.raetselgruppeDetails$.subscribe((raetselgruppe) => {
      this.#raetselgruppeID = raetselgruppe.id;
      this.#anzahlElemente = raetselgruppe.elemente.length;
    });
  }

  ngOnDestroy(): void {
      this.#raetselgruppeSubscription.unsubscribe();
  }

  getRaetselgruppeID(): string {
    return this.#raetselgruppeID;
  }

  gotoUebersicht(): void {
    this.raetselgruppenFacade.unselectRaetselgruppe();
  }

  openPrintVorschauDialog(): void {

    if (this.#raetselgruppeID.length === 0) {
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

        this.raetselgruppenFacade.generiereVorschau(this.#raetselgruppeID, layout);
      }
    });
  }

  generiereLaTeX(): void {
    this.raetselgruppenFacade.generiereLaTeX(this.#raetselgruppeID);
  }

  startEdit(): void {
    console.log('jetzt Editorkomponente aufmachen');
  }

  openNeuesRaetselgruppenelementDialog(): void {
    console.log('jetzt Dialog für neues Element anzeigen')
  }

  buttonsGenerierenDisabled(): boolean {
    return this.#raetselgruppeID.length === 0 || this.#anzahlElemente === 0;
  }
}
