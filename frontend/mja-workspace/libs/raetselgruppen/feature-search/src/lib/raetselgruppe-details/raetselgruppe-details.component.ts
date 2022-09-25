import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditRaetselgruppenelementPayload, RaetselgruppeDetails, RaetselgruppenFacade, RaetselgruppensucheTrefferItem } from '@mja-workspace/raetselgruppen/domain';
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

  raetselgruppeID: string | undefined;

  constructor(public raetselgruppenFacade: RaetselgruppenFacade, public dialog: MatDialog) { }



  ngOnInit(): void {

    this.#raetselgruppeSubscription = this.raetselgruppenFacade.raetselgruppeDetails$.pipe(
      tap((gruppe) => {
        if (gruppe) {
          this.raetselgruppeID = gruppe.id;
        } else {
          this.raetselgruppeID = undefined;
        }
      })
    ).subscribe();

  }

  ngOnDestroy(): void {

    this.#raetselgruppeSubscription.unsubscribe();
  }

  openNeuesRaetselgruppenelementDialog(): void {

    const dialogData: RaetselgruppenelementDialogData = {
      titel: 'Neues Element',
      id: 'neu',
      nummer: '',
      schluessel: '',
      punkte: 0
    };

    const dialogRef = this.dialog.open(RaetselgruppenelementDialogComponent, {
      height: '400px',
      width: '500px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && this.raetselgruppeID) {
        const data: RaetselgruppenelementDialogData = result as RaetselgruppenelementDialogData;
        this.#saveReaetselgruppeElement(this.raetselgruppeID, data);
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
