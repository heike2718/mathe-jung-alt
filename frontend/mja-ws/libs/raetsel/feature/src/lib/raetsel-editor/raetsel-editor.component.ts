import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { TextFieldModule } from '@angular/cdk/text-field';
import { MatExpansionModule } from '@angular/material/expansion';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { Antwortvorschlag, EditRaetselPayload, GrafikInfo, RaetselDetails } from '@mja-ws/raetsel/model';
import { combineLatest, Subscription } from 'rxjs';
import { ReactiveFormsModule, UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { anzeigeAntwortvorschlaegeSelectInput, DeskriptorUI, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, OUTPUTFORMAT, SelectableItem, SelectItemsCompomentModel, SelectGeneratorParametersUIModelAutoren, STATUS, fontNamenSelectInput, FONT_NAME, schriftgroessenSelectInput, SCHRIFTGROESSE } from '@mja-ws/core/model';
import { FrageLoesungImagesComponent, JaNeinDialogComponent, JaNeinDialogData, SelectItemsComponent, GeneratorParametersDialogAutorenComponent, SelectFileComponent, SelectFileModel, FileInfoComponent, FileInfoModel } from '@mja-ws/shared/components';
import { CoreFacade } from '@mja-ws/core/api';
import { GrafikFacade } from '@mja-ws/grafik/api';
import { GrafikDetailsComponent } from '../grafik-details/grafik-details.component';
import { MatCardModule } from '@angular/material/card';
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { EmbeddableImageContext, TEXTART } from '@mja-ws/embeddable-images/model';
import { EmbeddableImagesFacade } from '@mja-ws/embeddable-images/api';

interface AntwortvorschlagFormValue {
  text: string,
  korrekt: boolean;
};

@Component({
  selector: 'mja-raetsel-editor',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatSlideToggleModule,
    MatTooltipModule,
    CdkAccordionModule,
    TextFieldModule,
    MatExpansionModule,
    ReactiveFormsModule,
    FrageLoesungImagesComponent,
    SelectItemsComponent,
    JaNeinDialogComponent,
    GrafikDetailsComponent,
    GeneratorParametersDialogAutorenComponent,
    SelectFileComponent,
    FileInfoComponent
  ],
  templateUrl: './raetsel-editor.component.html',
  styleUrls: ['./raetsel-editor.component.scss'],
})
export class RaetselEditorComponent implements OnInit, OnDestroy {


  #raetselDetails!: RaetselDetails;
  #fb = inject(UntypedFormBuilder);

  #coreFacade = inject(CoreFacade);
  #authFacade = inject(AuthFacade);
  #embeddableImagesFacade = inject(EmbeddableImagesFacade);

  #combinedSubscription = new Subscription();

  #selectedDeskriptoren: DeskriptorUI[] = [];

  isRoot = false;

  anzahlenAntwortvorschlaege = ['0', '2', '3', '5', '6'];

  selectStatusInput: STATUS[] = ['ERFASST', 'FREIGEGEBEN'];

  selectItemsCompomentModel!: SelectItemsCompomentModel;
  dialog = inject(MatDialog);

  raetselFacade = inject(RaetselFacade);
  grafikFacade = inject(GrafikFacade);
  form!: UntypedFormGroup;

  fileInfoFrage: FileInfoModel | undefined;
  fileInfoLoesung: FileInfoModel | undefined;

  pfadGrafikFrage: string | undefined;
  pfadGrafikLoesung: string | undefined;

  selectFileFrageModel: SelectFileModel = {
    maxSizeBytes: 2097152,
    errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
    accept: '.eps',
    acceptMessage: 'erlaubte Dateitypen: eps',
    titel: 'Bild in Frage einbinden',
    beschreibung: 'Nach dem Hochladen erscheint der LaTeX-Befehl zum Einbinden der Grafik am Ende des Frage-Textes und kann an eine beliebiege Stelle verschoben werden.'
  };


  selectFileLoesungModel: SelectFileModel = {
    maxSizeBytes: 2097152,
    errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
    accept: '.eps',
    acceptMessage: 'erlaubte Dateitypen: eps',
    titel: 'Bild in Lösung einbinden',
    beschreibung: 'Nach dem Hochladen erscheint der LaTeX-Befehl zum Einbinden der Grafik am Ende des Lösung-Textes und kann an eine beliebiege Stelle verschoben werden.'
  };

  #embeddableImagesResponseSubscription: Subscription = new Subscription();

  constructor() {

    this.form = this.#fb.group({
      schluessel: ['', [Validators.pattern('^[0-9]{5}$')]],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      quelleId: ['', [Validators.required]],
      status: ['', [Validators.required]],
      frage: ['', [Validators.required]],
      loesung: [''],
      kommentar: [''],
      anzahlAntwortvorschlaege: ['0'],
      antwortvorschlaege: new UntypedFormArray([])
    });

  }

  ngOnInit(): void {

    this.#combinedSubscription = combineLatest([this.raetselFacade.raetselDetails$, this.#coreFacade.alleDeskriptoren$, this.#authFacade.userIsRoot$])
      .subscribe(([raetselDetails, alleDeskriptoren, root]) => {

        this.#raetselDetails = { ...raetselDetails };
        this.#selectedDeskriptoren = this.#raetselDetails.deskriptoren;
        this.selectItemsCompomentModel = this.raetselFacade.initSelectItemsCompomentModel(this.#raetselDetails.deskriptoren, alleDeskriptoren);

        this.isRoot = root;
        this.#initForm();
      });

    this.#embeddableImagesResponseSubscription = this.#embeddableImagesFacade.embeddableImageResponse$.subscribe(
      (responseDto) => {
        if (responseDto.context.textart === 'FRAGE') {

          const text = this.form.controls['frage'].value + '\n\n' + responseDto.includegraphicsCommand;
          this.form.controls['frage'].setValue(text);
          this.fileInfoFrage = undefined;
          this.pfadGrafikFrage = responseDto.pfad;
          this.#embeddableImagesFacade.resetState();
          // TODO: facade die Vorschau generieren lassen
        }
        if (responseDto.context.textart === 'LOESUNG') {

          const text = this.form.controls['loesung'].value + '\n\n' + responseDto.includegraphicsCommand;
          this.form.controls['loesung'].setValue(text);
          this.fileInfoLoesung = undefined;
          this.pfadGrafikLoesung = responseDto.pfad;
          this.#embeddableImagesFacade.resetState();
          // TODO: facade die Vorschau generieren lassen
        }
      }
    )
  }

  ngOnDestroy(): void {
    this.#combinedSubscription.unsubscribe();
    this.#embeddableImagesResponseSubscription.unsubscribe();
  }

  isFormValid(): boolean {
    if (this.form.valid) {
      return true;
    }
    return false;
  }

  getBuchstabe(index: number): string {
    switch (index) {
      case 0: return 'A';
      case 1: return 'B';
      case 2: return 'C';
      case 3: return 'D';
      case 4: return 'E';
      case 5: return 'F';
    }

    return '';
  }

  // convenience getters for easy access to form fields
  get avFormArray() { return this.form.controls['antwortvorschlaege'] as UntypedFormArray; }
  get antwortvorschlaegeFormGroup() { return this.avFormArray.controls as UntypedFormGroup[]; }

  raetselDataError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }

  antwortvorschlaegeErrors(): boolean {
    const antworten: Antwortvorschlag[] = this.#collectAntwortvorschlaege();

    if (antworten.length === 0) {
      return false;
    }

    let anzahlKorrekt = 0;

    antworten.forEach(a => {
      if (a.korrekt) {
        anzahlKorrekt++
      }
    });

    return anzahlKorrekt !== 1;
  }

  generierenDiabled(): boolean {

    const grafikInfosOhneFile: GrafikInfo[] = this.#raetselDetails.grafikInfos.filter(gi => !gi.existiert);
    return !this.form.valid || this.antwortvorschlaegeErrors() || grafikInfosOhneFile.length > 0;
  }

  onChangeAnzahlAntwortvorschlaege($event: any) {

    const anz = parseInt($event.target.value);
    this.#addOrRemoveAntowrtvorschlagFormParts(anz);
  }

  onSelectItemsCompomentModelChanged($event: SelectItemsCompomentModel) {

    if ($event) {
      const selectedItems: SelectableItem[] = $event.gewaehlteItems;
      this.#selectedDeskriptoren = [];
      selectedItems.forEach(item => this.#selectedDeskriptoren.push(
        {
          id: item.id as number,
          name: item.name
        }));
    }
  }

  cancelEdit() {
    if (this.#raetselDetails && this.#raetselDetails.id !== 'neu') {
      this.raetselFacade.selectRaetsel(this.#raetselDetails);
    } else {
      this.raetselFacade.cancelSelection();
    }
  }

  gotoSuche(): void {
    this.raetselFacade.cancelSelection();
  }

  printPNG(): void {
    const outputformat: OUTPUTFORMAT = 'PNG';
    this.#openPrintDialog(outputformat);
  }

  openHistorieLaTeXSpeichernDialog(raetsel: RaetselDetails): void {

    const dialogData: JaNeinDialogData = {
      frage: 'Soll Historie gespeichert werden?',
      hinweis: 'Bitte nur bei inhaltlichen Korrekturen. Das spart Speicherplatz'
    }

    const dialogRef = this.dialog.open(JaNeinDialogComponent, {
      height: '300px',
      width: '700px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      this.#doSubmit(raetsel, result);
    });
  }

  submit() {

    const raetsel: RaetselDetails = this.#readFormValues();

    const laTeXChanged = this.#latexChanged(raetsel);

    if (laTeXChanged) {
      this.openHistorieLaTeXSpeichernDialog(raetsel);
    } else {
      this.#doSubmit(raetsel, false);
    }
  }

  grafikLaden(link: string): void {
    this.grafikFacade.grafikPruefen(link);
  }

  downloadLatexLogs(): void {
    if (this.#raetselDetails && this.#raetselDetails.schluessel) {
      this.raetselFacade.downloadLatexLogs(this.#raetselDetails.schluessel)
    }
  }

  onFileSelected($event: FileInfoModel, textart: TEXTART): void {
    if (textart === 'FRAGE') {
      this.fileInfoFrage = $event;
    }
    if (textart === 'LOESUNG') {
      this.fileInfoLoesung = $event;
    }

  }

  uploadFile(textart: TEXTART): void {

    console.log('jetzt über die EmbeddableImageFacade die action up: ' + textart);

    const context: EmbeddableImageContext = {
      raetselId: this.#raetselDetails.id,
      textart: textart
    };

    if (textart === 'FRAGE' && this.fileInfoFrage) {
      this.#embeddableImagesFacade.createEmbeddableImage(context, this.fileInfoFrage!.file);
    }
    if (textart === 'LOESUNG' && this.fileInfoLoesung) {
      this.#embeddableImagesFacade.createEmbeddableImage(context, this.fileInfoLoesung!.file);
    }
  }

  #initForm() {

    const raetsel = this.#raetselDetails;

    this.form.controls['schluessel'].setValue(raetsel.schluessel);
    this.form.controls['name'].setValue(raetsel.name);
    this.form.controls['quelleId'].setValue(raetsel.quelle.id);
    this.form.controls['status'].setValue(raetsel.status);
    this.form.controls['frage'].setValue(raetsel.frage);
    this.form.controls['loesung'].setValue(raetsel.loesung);
    this.form.controls['kommentar'].setValue(raetsel.kommentar);
    this.form.controls['anzahlAntwortvorschlaege'].setValue(raetsel.antwortvorschlaege.length + '');

    this.#addOrRemoveAntowrtvorschlagFormParts(raetsel.antwortvorschlaege.length);

    if (!this.isRoot) {
      this.form.controls['schluessel'].disable();
    }

    for (let i = 0; i < raetsel.antwortvorschlaege.length; i++) {

      const avGroup = this.avFormArray.at(i);
      const av: Antwortvorschlag = raetsel.antwortvorschlaege[i];

      avGroup.setValue({ text: av.text, korrekt: av.korrekt });
    }
  }

  #addOrRemoveAntowrtvorschlagFormParts(anz: number) {

    if (this.avFormArray.length < anz) {

      for (let i = this.avFormArray.length; i < anz; i++) {
        this.avFormArray.push(this.#fb.group({
          text: ['', [Validators.required, Validators.maxLength(30)]],
          korrekt: [false, [Validators.required]]
        }))
      }
    } else {
      for (let i = this.avFormArray.length; i >= anz; i--) {
        this.avFormArray.removeAt(i);
      }
    }
  }

  #readFormValues(): RaetselDetails {
    const formValue = this.form.value;

    const antwortvorschlaegeNeu: Antwortvorschlag[] = this.#collectAntwortvorschlaege();

    const c_schluessel = formValue['schluessel'] ? formValue['schluessel'].trim() : ''

    const raetselDetails: RaetselDetails = {
      ...this.#raetselDetails,
      schluessel: c_schluessel,
      name: formValue['name'] !== null ? formValue['name'].trim() : '',
      status: formValue['status'],
      kommentar: formValue['kommentar'] !== null ? formValue['kommentar'].trim() : null,
      frage: formValue['frage'] !== null ? formValue['frage'].trim() : '',
      loesung: formValue['loesung'] !== null ? formValue['loesung'].trim() : null,
      antwortvorschlaege: antwortvorschlaegeNeu,
      deskriptoren: this.#selectedDeskriptoren,
      images: null
    };

    return raetselDetails;
  }

  #collectAntwortvorschlaege(): Antwortvorschlag[] {

    const result: Antwortvorschlag[] = [];

    for (let i = 0; i < this.avFormArray.length; i++) {
      const avFormValue = this.avFormArray.at(i).value as AntwortvorschlagFormValue;
      result.push({
        buchstabe: this.getBuchstabe(i),
        text: avFormValue.text.trim(),
        korrekt: avFormValue.korrekt
      });
    }
    return result;
  }

  #latexChanged(raetsel: RaetselDetails): boolean {

    return this.#raetselDetails.id !== 'neu' &&
      (raetsel.frage !== this.#raetselDetails.frage || raetsel.loesung !== this.#raetselDetails.loesung);
  }

  #doSubmit(raetsel: RaetselDetails, latexHistorisieren: boolean) {

    const editRaetselPayload: EditRaetselPayload = {
      latexHistorisieren: latexHistorisieren,
      raetsel: raetsel
    };

    this.raetselFacade.saveRaetsel(editRaetselPayload);
  }

  #openPrintDialog(outputformat: OUTPUTFORMAT): void {
    const dialogData: SelectGeneratorParametersUIModelAutoren = {
      titel: outputformat + ' generieren',
      showVerwendungszwecke: false,
      verwendungszwecke: [],
      selectedVerwendungszweck: undefined,
      layoutsAntwortvorschlaegeInput: anzeigeAntwortvorschlaegeSelectInput,
      selectedLayoutAntwortvorschlaege: undefined,
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

        if (dialogData.selectedLayoutAntwortvorschlaege) {

          let layout: LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'NOOP';
          switch (dialogData.selectedLayoutAntwortvorschlaege) {
            case 'Ankreuztabelle': layout = 'ANKREUZTABELLE'; break;
            case 'Buchstaben': layout = 'BUCHSTABEN'; break;
            case 'Liste': layout = 'DESCRIPTION'; break;
          }

          let font: FONT_NAME = 'STANDARD';
          let schriftgroesse: SCHRIFTGROESSE = 'NORMAL';

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
      }
    });
  }
}
