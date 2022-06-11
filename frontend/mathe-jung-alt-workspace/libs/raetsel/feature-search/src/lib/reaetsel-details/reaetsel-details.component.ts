import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { RaetselDetails, RaetselFacade, LATEX_OUTPUTFORMAT, anzeigeAntwortvorschlaegeSelectInput, LATEX_LAYOUT_ANTWORTVORSCHLAEGE } from '@mathe-jung-alt-workspace/raetsel/domain';
import { PrintRaetselDialogComponent, PrintRaetselDialogData } from '@mathe-jung-alt-workspace/raetsel/ui-raetsel';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-reaetsel-details',
  templateUrl: './reaetsel-details.component.html',
  styleUrls: ['./reaetsel-details.component.scss']
})
export class ReaetselDetailsComponent implements OnInit, OnDestroy {

  #raetselDetailsSubscription: Subscription = new Subscription();

  #raetselDetails!: RaetselDetails;

  constructor(public raetselFacade: RaetselFacade,
    public authFacade: AuthFacade,
    private quellenFacade: QuellenFacade,
    public dialog: MatDialog) { }


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
      this.raetselFacade.editRaetsel(this.#raetselDetails);
    }
  }

  openPrintPNGDialog(): void {
    this.openPrintDialog('PNG');
  }

  openPrintPDFDialog(): void {
    this.openPrintDialog('PDF');
  }

  private openPrintDialog(outputformat: LATEX_OUTPUTFORMAT): void {

    const dialogData: PrintRaetselDialogData = {
      titel: outputformat + ' generieren',
      layoutsAntwortvorschlaegeInput: anzeigeAntwortvorschlaegeSelectInput,
      selectedLayoutAntwortvorschlaege: undefined
    }

    const dialogRef = this.dialog.open(PrintRaetselDialogComponent, {
      height: '300px',
      width: '700px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && dialogData.selectedLayoutAntwortvorschlaege) {

        let layout: LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'NOOP';
        switch(dialogData.selectedLayoutAntwortvorschlaege) {
          case 'ANKREUZTABELLE': layout = 'ANKREUZTABELLE'; break;
          case 'BUCHSTABEN': layout = 'BUCHSTABEN'; break;
          case 'DESCRIPTION': layout = 'DESCRIPTION'; break;
        }

        this.raetselFacade.generiereRaetselOutput(this.#raetselDetails.id, outputformat, layout);
      }
    });
  }
}
