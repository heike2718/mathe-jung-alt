import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { QuellenFacade } from '@mja-workspace/quellen/domain';
import { anzeigeAntwortvorschlaegeSelectInput, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, LATEX_OUTPUTFORMAT, RaetselDetails, RaetselSearchFacade } from '@mja-workspace/raetsel/domain';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { PrintRaetselDialogComponent, PrintRaetselDialogData } from '@mja-workspace/shared/ui-raetsel';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-reaetsel-details',
  templateUrl: './raetsel-details.component.html',
  styleUrls: ['./raetsel-details.component.scss'],
})
export class RaetselDetailsComponent implements OnInit, OnDestroy {

  #raetselDetailsSubscription: Subscription = new Subscription();

  #raetselDetails!: RaetselDetails;


  constructor(public raetselSearchFacade: RaetselSearchFacade,
    public authFacade: AuthFacade,
    private quellenFacade: QuellenFacade,
    public dialog: MatDialog) { }

  ngOnInit(): void {

    this.#raetselDetailsSubscription = this.raetselSearchFacade.raetselDetails$.subscribe(
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

        // this.raetselFacade.generiereRaetselOutput(this.#raetselDetails.id, outputformat, layout);
      }
    });
  }

  startEdit(): void {}
}
