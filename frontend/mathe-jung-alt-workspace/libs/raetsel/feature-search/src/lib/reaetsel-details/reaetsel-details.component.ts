import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { RaetselDetails, RaetselFacade, LATEX_ANZEIGE_ANTWORTVORSCHLAEGE_TYP, LATEX_OUTPUTFORMAT, anzeigeAntwortvorschlaegeSelectInput } from '@mathe-jung-alt-workspace/raetsel/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { Subscriber, Subscription } from 'rxjs';
import { PrintRaetselDialogComponent } from '../print-raetsel-dialog/print-raetsel-dialog.component';

@Component({
  selector: 'mja-reaetsel-details',
  templateUrl: './reaetsel-details.component.html',
  styleUrls: ['./reaetsel-details.component.scss']
})
export class ReaetselDetailsComponent implements OnInit, OnDestroy {

  #raetselDetailsSubscription: Subscription = new Subscription();

  #raetselDetails!: RaetselDetails;

  #printAntwortvorschlaegeTyp: string | undefined;

  constructor(public raetselFacade: RaetselFacade, public authFacade: AuthFacade, private quellenFacade: QuellenFacade, public dialog: MatDialog) { }


  ngOnInit(): void {

    this.#raetselDetailsSubscription = this.raetselFacade.raetselDetails$.subscribe(
      details => {
        if (details) {
          this.#raetselDetails = details;
        }
      }
    );

  }

  ngOnDestroy(): void {
    this.#raetselDetailsSubscription.unsubscribe();
  }

  startEdit(): void {
    if (this.#raetselDetails) {
      this.quellenFacade.loadQuelle(this.#raetselDetails.quelleId);
      this.raetselFacade.startEditRaetsel(this.#raetselDetails);
    }
  }

  openPrintPNGDialog(): void {

    const outputformat: LATEX_OUTPUTFORMAT = 'PNG';

    const dialogRef = this.dialog.open(PrintRaetselDialogComponent, {
      width: '450px',
      data: {
        title: outputformat + ' generieren',
        anzeigeAntwortvorschlaegeInput: anzeigeAntwortvorschlaegeSelectInput,
        selectedAnzeigeAntwortvorschlaege: '--'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.#printAntwortvorschlaegeTyp = result;

      console.log(this.#printAntwortvorschlaegeTyp);
    });

  }
}
