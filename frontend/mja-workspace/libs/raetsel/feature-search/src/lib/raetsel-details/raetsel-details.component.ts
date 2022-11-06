import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { GrafikFacade } from '@mja-workspace/grafik/domain';
import { QuellenFacade } from '@mja-workspace/quellen/domain';
import { anzeigeAntwortvorschlaegeSelectInput, GrafikInfo, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, LATEX_OUTPUTFORMAT, RaetselDetails, RaetselFacade } from '@mja-workspace/raetsel/domain';
import { AuthFacade } from '@mja-workspace/shared/auth/domain';
import { PrintRaetselDialogComponent, PrintRaetselDialogData } from '@mja-workspace/shared/ui-components';
import { STORAGE_KEY_QUELLE } from '@mja-workspace/shared/util-configuration';
import { Message } from '@mja-workspace/shared/util-mja';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-reaetsel-details',
  templateUrl: './raetsel-details.component.html',
  styleUrls: ['./raetsel-details.component.scss'],
})
export class RaetselDetailsComponent implements OnInit, OnDestroy {

  #raetselDetailsSubscription: Subscription = new Subscription();

  #raetselDetails!: RaetselDetails;


  constructor(public raetselFacade: RaetselFacade,
    public authFacade: AuthFacade,
    public quellenFacade: QuellenFacade,
    private router: Router,
    public dialog: MatDialog,
    public grafikFacade: GrafikFacade) { }

  ngOnInit(): void {

    this.#raetselDetailsSubscription = this.raetselFacade.raetselDetails$.subscribe(
      details => {
        if (details) {
          this.#raetselDetails = details;
          this.quellenFacade.loadQuelle(details.quelleId);
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

  gotoRaetselUebersicht(): void {
    this.router.navigateByUrl('/raetsel/uebersicht');
  }

  isEigenesRaetsel(): boolean {

    const storedQuelle = localStorage.getItem(STORAGE_KEY_QUELLE);
    if (!storedQuelle) {
      return false;
    }
    const quelle = JSON.parse(storedQuelle);
    return quelle.id === this.#raetselDetails.quelleId;
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
        switch (dialogData.selectedLayoutAntwortvorschlaege) {
          case 'ANKREUZTABELLE': layout = 'ANKREUZTABELLE'; break;
          case 'BUCHSTABEN': layout = 'BUCHSTABEN'; break;
          case 'DESCRIPTION': layout = 'DESCRIPTION'; break;
        }

        this.raetselFacade.generiereRaetselOutput(this.#raetselDetails.id, outputformat, layout);
      }
    });
  }

  grafikLaden(link: string): void {
    this.grafikFacade.grafikPruefen(link);
  }

  generierenDiabled(): boolean {

    const grafikInfosOhneFile: GrafikInfo[] = this.#raetselDetails.grafikInfos.filter(gi => !gi.existiert);

    return grafikInfosOhneFile.length > 0;
  }

  onGrafikHochgeladen($event: Message): void {
    if ($event.level === 'INFO') {
      this.raetselFacade.selectRaetsel(this.#raetselDetails);
    }
  }
}
