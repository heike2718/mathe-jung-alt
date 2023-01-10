import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { TextFieldModule } from '@angular/cdk/text-field';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatExpansionModule } from '@angular/material/expansion';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { Router } from '@angular/router';
import { GrafikInfo, RaetselDetails } from '@mja-ws/raetsel/model';
import { Subscription, tap } from 'rxjs';
import { FileUploadComponent, FrageLoesungImagesComponent, SelectPrintparametersDialogComponent } from '@mja-ws/shared/components';
import { AntwortvorschlagComponent } from '../antwortvorschlag/antwortvorschlag.component';
import { Message } from '@mja-ws/shared/messaging/api';
import { GrafikFacade } from '@mja-ws/grafik/api';
import { GrafikDetailsComponent } from '../grafik-details/grafik-details.component';
import { anzeigeAntwortvorschlaegeSelectInput, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, OUTPUTFORMAT, SelectPrintparametersDialogData } from '@mja-ws/core/model';

@Component({
  selector: 'mja-raetsel-details',
  standalone: true,
  imports: [
    CommonModule,
    CdkAccordionModule,
    FlexLayoutModule,
    MatExpansionModule,
    MatChipsModule,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    MatFormFieldModule,
    MatListModule,
    MatTooltipModule,
    TextFieldModule,
    FrageLoesungImagesComponent,
    AntwortvorschlagComponent,
    FileUploadComponent,
    GrafikDetailsComponent,
    SelectPrintparametersDialogComponent
  ],
  templateUrl: './raetsel-details.component.html',
  styleUrls: ['./raetsel-details.component.scss'],
})
export class RaetselDetailsComponent implements OnInit, OnDestroy {

  public raetselFacade = inject(RaetselFacade);
  public authFacade = inject(AuthFacade);
  public grafikFacade = inject(GrafikFacade);
  public dialog = inject(MatDialog);

  #router = inject(Router);

  #raetselDetailsSubscription = new Subscription();
  #raetselDetails!: RaetselDetails;

  ngOnInit(): void {

    this.#raetselDetailsSubscription = this.raetselFacade.raetselDetails$.pipe(
      tap((details) => {
        console.log('raetselDetails changed');
        this.#raetselDetails = details;
      })
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.#raetselDetailsSubscription.unsubscribe();
  }

  startEdit(): void {
    if (this.#raetselDetails) {
      this.raetselFacade.editRaetsel();
    }
  }

  openPrintPNGDialog(): void {
    this.#openPrintDialog('PNG');
  }

  openPrintPDFDialog(): void {
    this.#openPrintDialog('PDF');
  }

  gotoRaetselUebersicht(): void {
    this.#router.navigateByUrl('/raetsel');
  }

  generierenDiabled(): boolean {
    const grafikInfosOhneFile: GrafikInfo[] = this.#raetselDetails.grafikInfos.filter(gi => !gi.existiert);
    return grafikInfosOhneFile.length > 0;
  }

  grafikLaden(link: string): void {
    this.grafikFacade.grafikPruefen(link);
  }

  onGrafikHochgeladen($event: Message): void {
    if ($event.level === 'INFO') {
      this.raetselFacade.selectRaetsel(this.#raetselDetails);
    }
  }

  #openPrintDialog(outputformat: OUTPUTFORMAT): void {

    const dialogData: SelectPrintparametersDialogData = {
      titel: outputformat + ' generieren',
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

        this.raetselFacade.generiereRaetselOutput(this.#raetselDetails.id, outputformat, layout);
      }
    });
  }
}
