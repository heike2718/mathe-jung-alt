import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { TextFieldModule } from '@angular/cdk/text-field';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FormsModule } from '@angular/forms';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { AuthFacade } from '@mja-ws/core/api';
import { Router } from '@angular/router';
import { RaetselDetails } from '@mja-ws/raetsel/model';
import { Subscription, tap } from 'rxjs';
import { FrageLoesungImagesComponent, GeneratorParametersDialogAutorenComponent } from '@mja-ws/shared/components';
import { AntwortvorschlagComponent } from '../antwortvorschlag/antwortvorschlag.component';
import { EmbeddableImageVorschauComponent } from '../embeddable-image-vorschau/embeddable-image-vorschau.component';
import {
  anzeigeAntwortvorschlaegeSelectInput,
  FontName,
  fontNamenSelectInput,
  LaTeXLayoutAntwortvorschlaege,
  OutputFormat,
  Schriftgroesse,
  schriftgroessenSelectInput,
  SelectGeneratorParametersUIModelAutoren,
  User
} from '@mja-ws/core/model';
import { EmbeddableImagesFacade } from '@mja-ws/embeddable-images/api';
import { Configuration } from '@mja-ws/shared/config';
import { EmbeddableImageInfoComponent } from '../embeddable-image-info/embeddable-image-info.component';
import { EmbeddableImageInfo } from '@mja-ws/embeddable-images/model';
import { AufgabensammlungenFacade } from '@mja-ws/aufgabensammlungen/api';
import { AufgabensammlungDetails, AufgabensammlungTrefferItem } from '@mja-ws/aufgabensammlungen/model';

@Component({
  selector: 'mja-raetsel-details',
  standalone: true,
  imports: [
    CommonModule,
    CdkAccordionModule,
    FormsModule,
    MatExpansionModule,    
    MatButtonModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatListModule,
    MatTooltipModule,
    TextFieldModule,
    FrageLoesungImagesComponent,
    AntwortvorschlagComponent,
    EmbeddableImageVorschauComponent,
    EmbeddableImageInfoComponent,
    GeneratorParametersDialogAutorenComponent
  ],
  templateUrl: './raetsel-details.component.html',
  styleUrls: ['./raetsel-details.component.scss'],
})
export class RaetselDetailsComponent implements OnInit, OnDestroy {

  raetselFacade = inject(RaetselFacade);
  aufgabensammlungenFacade = inject(AufgabensammlungenFacade);
  #authFacade = inject(AuthFacade);
  dialog = inject(MatDialog);
  freigegeben = false;
  user!: User;

  #config = inject(Configuration);
  devMode = !this.#config.production;

  #embeddableImagesFacade = inject(EmbeddableImagesFacade);
  #router = inject(Router);

  #raetselDetailsSubscription = new Subscription();
  #raetselDetails!: RaetselDetails;
  #aufgabensammlungDetailsSubscription = new Subscription();
  #userSubscription = new Subscription();

  #selectedAufgabensammlung: AufgabensammlungDetails | undefined;

  ngOnInit(): void {  

    this.#raetselDetailsSubscription = this.raetselFacade.raetselDetails$.pipe(
      tap((details: RaetselDetails) => {
        this.#raetselDetails = details;
        this.freigegeben = this.#raetselDetails.freigegeben;
      })
    ).subscribe();

    this.#aufgabensammlungDetailsSubscription = this.aufgabensammlungenFacade.aufgabensammlungDetails$
      .subscribe((aufgabensammlung) => this.#selectedAufgabensammlung = aufgabensammlung);

      this.#userSubscription = this.#authFacade.user$.subscribe((user) => this.user = user);
  }

  ngOnDestroy(): void {
    this.#raetselDetailsSubscription.unsubscribe();
    this.#embeddableImagesFacade.clearVorschau();
    this.#aufgabensammlungDetailsSubscription.unsubscribe();
    this.#userSubscription.unsubscribe();
  }

  startEdit(): void {
    if (this.#raetselDetails) {
      this.raetselFacade.editRaetsel(this.#raetselDetails);
    }
  }

  printPNG(): void {

    const outputformat: OutputFormat = 'PNG';
    this.#openPrintDialog(outputformat);
  }

  printPDF(): void {

    const outputformat: OutputFormat = 'PDF';
    this.#openPrintDialog(outputformat);
  }

  gotoRaetselUebersicht(): void {
    this.#router.navigateByUrl('/raetsel');
  }

  gotoAufgabensammlung(): void {

    if (this.#selectedAufgabensammlung) {
      const trefferitem: AufgabensammlungTrefferItem = {
        anzahlElemente: this.#selectedAufgabensammlung.elemente.length,
        geaendertDurch: this.#selectedAufgabensammlung.geaendertDurch,
        id: this.#selectedAufgabensammlung.id,
        name: this.#selectedAufgabensammlung.name,
        referenz: this.#selectedAufgabensammlung.referenz,
        referenztyp: this.#selectedAufgabensammlung.referenztyp,
        schwierigkeitsgrad: this.#selectedAufgabensammlung.schwierigkeitsgrad,
        freigegeben: this.#selectedAufgabensammlung.freigegeben,
        privat: this.#selectedAufgabensammlung.privat
      };
      this.aufgabensammlungenFacade.selectAufgabensammlung(trefferitem)
    } else {
      this.#router.navigateByUrl('aufgabensammlungen');
    }
  }

  generierenDiabled(): boolean {
    const mbeddableImageInfosOhneFile: EmbeddableImageInfo[] = this.#raetselDetails.embeddableImageInfos.filter(gi => !gi.existiert);
    return mbeddableImageInfosOhneFile.length > 0;
  }

  downloadLatexLogs(): void {
    if (this.#raetselDetails) {
      this.raetselFacade.downloadLatexLogs(this.#raetselDetails.schluessel)
    }
  }

  downloadEmbeddedImages(): void {
    if (this.#raetselDetails && this.#raetselDetails.embeddableImageInfos.length > 0) {
      this.raetselFacade.downloadEmbeddedImages(this.#raetselDetails.id);
    }
  }

  downloadRaetselLaTeX(): void {
    if (this.#raetselDetails) {
      this.raetselFacade.downloadRaetselLaTeX(this.#raetselDetails.id);
    }
  }

  #openPrintDialog(outputformat: OutputFormat): void {

    const dialogData: SelectGeneratorParametersUIModelAutoren = {
      titel: outputformat + ' generieren',
      showVerwendungszwecke: false,
      verwendungszwecke: [],
      selectedVerwendungszweck: undefined,
      layoutsAntwortvorschlaegeInput: anzeigeAntwortvorschlaegeSelectInput,
      selectedLayoutAntwortvorschlaege: outputformat === 'PNG' ? 'Liste' : undefined,
      fontNamen: fontNamenSelectInput,
      selectedFontName: undefined,
      schriftgroessen: schriftgroessenSelectInput,
      selectedSchriftgroesse: undefined
    };

    const dialogRef = this.dialog.open(GeneratorParametersDialogAutorenComponent, {
      height: '600px',
      width: '700px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {

        let layout: LaTeXLayoutAntwortvorschlaege = 'NOOP';

        if (dialogData.selectedLayoutAntwortvorschlaege) {

          switch (dialogData.selectedLayoutAntwortvorschlaege) {
            case 'Ankreuztabelle': layout = 'ANKREUZTABELLE'; break;
            case 'Buchstaben': layout = 'BUCHSTABEN'; break;
            case 'Liste': layout = 'DESCRIPTION'; break;
          }
        }

        let font: FontName = 'STANDARD';
        let schriftgroesse: Schriftgroesse = 'NORMAL';

        if (dialogData.selectedFontName) {
          switch (dialogData.selectedFontName) {
            case 'Druckschrift (Leseanfänger)': font = 'DRUCK_BY_WOK'; break;
            case 'Fibel Nord': font = 'FIBEL_NORD'; break;
            case 'Fibel Süd': font = 'FIBEL_SUED'; break;
          }
        }

        if (dialogData.selectedSchriftgroesse) {
          switch (dialogData.selectedSchriftgroesse) {
            case 'sehr groß': schriftgroesse = 'HUGE'; break;
            case 'groß': schriftgroesse = 'LARGE'; break;
          }
        }

        this.raetselFacade.generiereRaetselOutput(this.#raetselDetails.id, outputformat, font, schriftgroesse, layout);
      }
    });
  }
}
