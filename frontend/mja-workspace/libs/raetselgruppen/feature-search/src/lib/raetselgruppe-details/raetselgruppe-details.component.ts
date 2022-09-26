import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditRaetselgruppenelementPayload, RaetselgruppeBasisdaten, RaetselgruppeDetails, Raetselgruppenelement, RaetselgruppenFacade, RaetselgruppensucheTrefferItem } from '@mja-workspace/raetselgruppen/domain';
import { JaNeinDialogComponent, JaNeinDialogData } from '@mja-workspace/shared/ui-components';
import { Subscription, tap } from 'rxjs';
import { RaetselgruppenelementDialogComponent } from '../raetselgruppenelement-dialog/raetselgruppenelement-dialog.component';
import { RaetselgruppenelementDialogData } from '../raetselgruppenelement-dialog/raetselgruppenelement-dialog.data';

@Component({
  selector: 'mja-raetselgruppe',
  templateUrl: './raetselgruppe-details.component.html',
  styleUrls: ['./raetselgruppe-details.component.scss'],
})
export class RaetselgruppeDetailsComponent implements OnInit, OnDestroy {

  #raetselgruppeSubscription = new Subscription();
  #raetselgruppeBasidaten?: RaetselgruppeBasisdaten;

  constructor(public raetselgruppenFacade: RaetselgruppenFacade, public dialog: MatDialog) { }

  ngOnInit(): void {

    this.#raetselgruppeSubscription = this.raetselgruppenFacade.raetselgruppeDetails$.pipe(
      tap((gruppe) => this.#raetselgruppeBasidaten = gruppe)
    ).subscribe();

  }

  ngOnDestroy(): void {

    this.#raetselgruppeSubscription.unsubscribe();
  }

  getRaetselgruppeID(): string | undefined {
    return this.#raetselgruppeBasidaten ? this.#raetselgruppeBasidaten.id : undefined;
  }

  startEdit(): void {
    if (this.#raetselgruppeBasidaten) {
      this.raetselgruppenFacade.editRaetselgruppe(this.#raetselgruppeBasidaten);
    }
  }

  gotoUebersicht(): void {
    this.raetselgruppenFacade.unselectRaetselgruppe();
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
    if (this.#raetselgruppeBasidaten) {
      this.#openConfirmLoeschenDialog(this.#raetselgruppeBasidaten.id, $element);
    }
  }

  #openConfirmLoeschenDialog(raetselgruppeID: string, element: Raetselgruppenelement): void {

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
        this.raetselgruppenFacade.deleteRaetselgruppenelement(raetselgruppeID, element);
      }
    });
  }

  #initAndOpenEditElementDialog(dialogData: RaetselgruppenelementDialogData): void {
    const dialogRef = this.dialog.open(RaetselgruppenelementDialogComponent, {
      height: '400px',
      width: '500px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && this.#raetselgruppeBasidaten) {
        const data: RaetselgruppenelementDialogData = result as RaetselgruppenelementDialogData;
        this.#saveReaetselgruppeElement(this.#raetselgruppeBasidaten.id, data);
      }
    });
  }

  #saveReaetselgruppeElement(gruppeID: string, data: RaetselgruppenelementDialogData): void {
    const payload: EditRaetselgruppenelementPayload = {
      id: data.id,
      raetselSchluessel: data.schluessel,
      nummer: data.nummer,
      punkte: data.punkte
    };

    this.raetselgruppenFacade.saveRaetselgruppenelement(gruppeID, payload);

  }
}
