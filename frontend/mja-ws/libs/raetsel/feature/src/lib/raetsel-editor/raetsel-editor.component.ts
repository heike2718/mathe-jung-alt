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
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { TextFieldModule } from '@angular/cdk/text-field';
import { MatExpansionModule } from '@angular/material/expansion';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { Antwortvorschlag, EditRaetselPayload, GuiHerkunfsttyp, GuiHerkunftstypenMap, GuiQuellenart, GuiQuellenartenMap, MediumQuelleDto } from '@mja-ws/raetsel/model';
import { combineLatest, Subject, Subscription } from 'rxjs';
import { FormsModule, ReactiveFormsModule, UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import {
  anzeigeAntwortvorschlaegeSelectInput,
  DeskriptorUI,
  LaTeXLayoutAntwortvorschlaege,
  OutputFormat,
  SelectableItem,
  SelectItemsCompomentModel,
  SelectGeneratorParametersUIModelAutoren,
  fontNamenSelectInput,
  FontName,
  schriftgroessenSelectInput,
  Schriftgroesse,
  Herkunftstyp,
  QuelleDto,
  Quellenart,
  initialQuelleDto
} from '@mja-ws/core/model';
import {
  FrageLoesungImagesComponent,
  JaNeinDialogComponent,
  JaNeinDialogData,
  SelectItemsComponent,
  GeneratorParametersDialogAutorenComponent,
  SelectFileComponent,
  SelectFileModel,
  FileInfoComponent,
  FileInfoModel,
  ImageDialogComponent,
  ImageDialogModel
} from '@mja-ws/shared/components';
import { CoreFacade } from '@mja-ws/core/api';
import { EmbeddableImageVorschauComponent } from '../embeddable-image-vorschau/embeddable-image-vorschau.component';
import { MatCardModule } from '@angular/material/card';
import { AuthFacade } from '@mja-ws/core/api';
import { EmbeddableImageContext, EmbeddableImageInfo, EmbeddableImageVorschau, Textart } from '@mja-ws/embeddable-images/model';
import { EmbeddableImagesFacade } from '@mja-ws/embeddable-images/api';
import { EmbeddableImageInfoComponent } from '../embeddable-image-info/embeddable-image-info.component';

interface AntwortvorschlagFormValue {
  text: string,
  korrekt: boolean;
};

@Component({
  selector: 'mja-raetsel-editor',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatTooltipModule,
    CdkAccordionModule,
    TextFieldModule,
    ReactiveFormsModule,
    FrageLoesungImagesComponent,
    SelectItemsComponent,
    JaNeinDialogComponent,
    EmbeddableImageVorschauComponent,
    EmbeddableImageInfoComponent,
    GeneratorParametersDialogAutorenComponent,
    ImageDialogComponent,
    SelectFileComponent,
    FileInfoComponent,
  ],
  templateUrl: './raetsel-editor.component.html',
  styleUrls: ['./raetsel-editor.component.scss'],
})
export class RaetselEditorComponent implements OnInit, OnDestroy {

  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //   QUELLENTEIL
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  searchTerm = '';
  quelle!: QuelleDto;
  quellenangabe = '';

  selectQuellenartInput: string[] = new GuiQuellenartenMap().getLabelsSorted();
  #selectedQuellenart: string = '';

  #mediumUuid: string | undefined;

  showMediensuche = false;
  showKlasse = false;
  showStufe = false;
  showAusgabe = false;
  showJahr = false;
  showSeite = false;
  showPerson = false;
  showPfad = false;


  #autor!: QuelleDto;


  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //   RAETSELTEIL
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  #infoIncludegraphics = 'Nach dem Hochladen erscheint der LaTeX-Befehl zum Einbinden der Grafik am Ende des Textes und kann an eine beliebiege Stelle verschoben werden. Das width-Attribut kann geändert werden. Um eine eingebundene Grafik wieder zu löschen, bitte einfach den \\includegraphics-Befehl aus dem Text entfernen und speichern. Dabei wird auf dem Server auch die Grafikdatei gelöscht.';
  #warnungIncludegraphics = 'Bitte den \\includegraphics-Befehl nicht manuell einfügen. Er wird beim Hochladen einer neuen Datei vom System generiert. Der generierte Pfad darf nicht geändert werden!';

  #fb = inject(UntypedFormBuilder);

  #coreFacade = inject(CoreFacade);
  #authFacade = inject(AuthFacade);
  #embeddableImagesFacade = inject(EmbeddableImagesFacade);

  #combinedSubscription = new Subscription();

  #selectedDeskriptoren: DeskriptorUI[] = [];
  selectedHerkunftstyp!: Herkunftstyp;

  isRoot = false;


  #editRaetselPayload!: EditRaetselPayload;
  #editRaetselPayloadCache!: EditRaetselPayload;

  anzahlenAntwortvorschlaege = ['0', '2', '3', '5', '6'];

  selectStatusInput: string[] = ['ERFASST', 'FREIGEGEBEN'];
  selectHerkunftstypInput: string[] = new GuiHerkunftstypenMap().getLabelsSorted();

  selectItemsCompomentModel!: SelectItemsCompomentModel;
  dialog = inject(MatDialog);

  raetselFacade = inject(RaetselFacade);
  form!: UntypedFormGroup;

  fileInfoFrage: FileInfoModel | undefined;
  fileInfoLoesung: FileInfoModel | undefined;

  pfadGrafikFrage: string | undefined;
  pfadGrafikLoesung: string | undefined;

  embeddableImageInfosFrage: EmbeddableImageInfo[] = [];
  embeddableImageInfosLoesung: EmbeddableImageInfo[] = [];

  panelGrafikenFrageOpen = false;
  panelGrafikenLoesungOpen = false;

  selectFileFrageModel: SelectFileModel = {
    maxSizeBytes: 2097152,
    errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
    accept: '.eps',
    acceptMessage: 'erlaubte Dateitypen: eps',
    titel: 'neues Bild in Frage einbinden (ersetzen ist nur in der Detailansicht möglich)',
    beschreibung: this.#infoIncludegraphics,
    hinweis: this.#warnungIncludegraphics
  };

  selectFileLoesungModel: SelectFileModel = {
    maxSizeBytes: 2097152,
    errorMessageSize: 'Die Datei ist zu groß. Die maximale erlaubte Größe ist 2 MB.',
    accept: '.eps',
    acceptMessage: 'erlaubte Dateitypen: eps',
    titel: 'neues Bild in Lösung einbinden (ersetzen ist nur in der Detailansicht möglich)',
    beschreibung: this.#infoIncludegraphics,
    hinweis: this.#warnungIncludegraphics
  };

  #embeddableImagesResponseSubscription: Subscription = new Subscription();
  #combinedEmbeddableImageVorschauSubscription: Subscription = new Subscription();


  constructor() {

    this.form = this.#fb.group({
      schluessel: ['', [Validators.pattern('^[0-9]{5}$')]],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      herkunftstyp: ['', [Validators.required]],
      herkunft: [{ value: '', disabled: true }],
      status: ['', [Validators.required]],
      frage: ['', [Validators.required]],
      loesung: [''],
      kommentar: ['', [Validators.maxLength(200)]],
      anzahlAntwortvorschlaege: ['0'],
      antwortvorschlaege: new UntypedFormArray([]),  // ab hier die Teile für die Quelle
      quellenart: [''],
      medium: [''],
      person: ['', [Validators.maxLength(100)]],
      jahr: ['', [Validators.maxLength(4)]],
      ausgabe: ['', [Validators.maxLength(10)]],
      seite: ['', [Validators.maxLength(4)]],
      klasse: ['', [Validators.maxLength(10)]],
      stufe: ['', [Validators.maxLength(10)]],
      pfad: ['', [Validators.maxLength(500)]],
    });

  }

  ngOnInit(): void {

    this.#combinedSubscription = combineLatest([this.raetselFacade.raetselDetails$,
    this.#coreFacade.alleDeskriptoren$,
    this.#coreFacade.autor$,
    this.#authFacade.userIsRoot$
    ])
      .subscribe(([raetselDetails,
        alleDeskriptoren,
        autor,
        root
      ]) => {

        this.quelle = { ...raetselDetails.quelle };
        this.quellenangabe = raetselDetails.quellenangabe;
        this.#mediumUuid = this.quelle.mediumUuid;
        this.#autor = autor;

        const theSchluessel = raetselDetails.schluessel.length > 0 ? raetselDetails.schluessel : null;
        this.selectedHerkunftstyp = raetselDetails.herkunftstyp;

        this.#editRaetselPayload = {
          latexHistorisieren: false,
          antwortvorschlaege: raetselDetails.antwortvorschlaege,
          deskriptoren: raetselDetails.deskriptoren,
          frage: raetselDetails.frage,
          freigegeben: raetselDetails.freigegeben,
          herkunftstyp: this.selectedHerkunftstyp,
          id: raetselDetails.id,
          kommentar: raetselDetails.kommentar,
          loesung: raetselDetails.loesung,
          name: raetselDetails.name,
          schluessel: theSchluessel,
          quelle: this.quelle
        };

        this.#editRaetselPayloadCache = { ...this.#editRaetselPayload };
        this.#selectedDeskriptoren = raetselDetails.deskriptoren;
        this.selectItemsCompomentModel = this.raetselFacade.initSelectItemsCompomentModel(raetselDetails.deskriptoren, alleDeskriptoren);
        this.embeddableImageInfosFrage = raetselDetails.embeddableImageInfos.filter((info) => info.textart === 'FRAGE');
        this.embeddableImageInfosLoesung = raetselDetails.embeddableImageInfos.filter((info) => info.textart === 'LOESUNG');
        this.selectedHerkunftstyp = raetselDetails.herkunftstyp;
        this.#selectedQuellenart = new GuiQuellenartenMap().getLabelOfQuellenart(this.quelle.quellenart);

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
        }
        if (responseDto.context.textart === 'LOESUNG') {

          const text = this.form.controls['loesung'].value + '\n\n' + responseDto.includegraphicsCommand;
          this.form.controls['loesung'].setValue(text);
          this.fileInfoLoesung = undefined;
          this.pfadGrafikLoesung = responseDto.pfad;
          this.#embeddableImagesFacade.resetState();
        }
      }
    );

    this.#combinedEmbeddableImageVorschauSubscription = combineLatest([this.#embeddableImagesFacade.selectedEmbeddableImageInfo$, this.#embeddableImagesFacade.selectedEmbeddableImageVorschau$])
      .subscribe(([imageInfo, imageVorschau]) => {

        if (imageInfo.existiert && imageInfo.pfad === imageVorschau.pfad) {
          if (imageInfo.textart === 'FRAGE' && this.panelGrafikenFrageOpen) {
            this.#openEmbeddableImageVorschauDialog(imageVorschau);
          }
          if (imageInfo.textart === 'LOESUNG' && this.panelGrafikenLoesungOpen) {
            this.#openEmbeddableImageVorschauDialog(imageVorschau);
          }
        }
      });
  }

  ngOnDestroy(): void {
    this.#combinedSubscription.unsubscribe();
    this.#embeddableImagesResponseSubscription.unsubscribe();
    this.#combinedEmbeddableImageVorschauSubscription.unsubscribe();
    this.#embeddableImagesFacade.clearVorschau();
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

    const embeddableImageInfosFrageOhneFile: EmbeddableImageInfo[] = this.embeddableImageInfosFrage.filter(gi => !gi.existiert);
    const embeddableImageInfosLoesungOhneFile: EmbeddableImageInfo[] = this.embeddableImageInfosLoesung.filter(gi => !gi.existiert);
    return !this.form.valid || this.antwortvorschlaegeErrors() || embeddableImageInfosFrageOhneFile.length > 0 || embeddableImageInfosLoesungOhneFile.length > 0;
  }

  // ================================================================================================================
  // initialization methods
  // ================================================================================================================

  #initForm() {

    const theStatus = this.#editRaetselPayload.freigegeben ? 'FREIGEGEBEN' : 'ERFASST';

    const theGuiHerkunftstyp: GuiHerkunfsttyp = new GuiHerkunftstypenMap().getGuiHerkunftstyp(this.#editRaetselPayload.herkunftstyp);
    const theGuiQuellenart: GuiQuellenart = new GuiQuellenartenMap().getGuiQuellenart(this.quelle.quellenart);

    this.form.get('schluessel')?.setValue(this.#editRaetselPayload.schluessel);
    this.form.get('name')?.setValue(this.#editRaetselPayload.name);
    this.form.get('herkunftstyp')?.setValue(theGuiHerkunftstyp.label);
    this.form.get('status')?.setValue(theStatus);
    this.form.get('frage')?.setValue(this.#editRaetselPayload.frage);
    this.form.get('loesung')?.setValue(this.#editRaetselPayload.loesung);
    this.form.get('kommentar')?.setValue(this.#editRaetselPayload.kommentar);
    this.form.get('quellenart')?.setValue(theGuiQuellenart.label);

    this.form.get('person')?.setValue(this.quelle.person ? this.quelle.person : '');
    this.form.get('jahr')?.setValue(this.quelle.jahr ? this.quelle.jahr : '');
    this.form.get('ausgabe')?.setValue(this.quelle.ausgabe ? this.quelle.ausgabe : '');
    this.form.get('seite')?.setValue(this.quelle.seite ? this.quelle.seite : '');
    this.form.get('klasse')?.setValue(this.quelle.klasse ? this.quelle.klasse : '');
    this.form.get('stufe')?.setValue(this.quelle.stufe ? this.quelle.stufe : '');
    this.form.get('pfad')?.setValue(this.quelle.pfad ? this.quelle.pfad : '');

    this.form.get('anzahlAntwortvorschlaege')?.setValue(this.#editRaetselPayload.antwortvorschlaege.length + '');

    this.#addOrRemoveAntowrtvorschlagFormParts(this.#editRaetselPayload.antwortvorschlaege.length);

    if (!this.isRoot) {
      this.form.get('schluessel')?.disable();
    }

    for (let i = 0; i < this.#editRaetselPayload.antwortvorschlaege.length; i++) {

      const avGroup = this.avFormArray.at(i);
      const av: Antwortvorschlag = this.#editRaetselPayload.antwortvorschlaege[i];

      avGroup.setValue({ text: av.text, korrekt: av.korrekt });
    }

    this.#initVisibilityQuelleInputs();
  }

  #initVisibilityQuelleInputs(): void {
    const quellenart: Quellenart = new GuiQuellenartenMap().getQuellenartOfLabel(this.#selectedQuellenart);

    switch (quellenart) {
      case 'BUCH': {

        this.showAusgabe = false;
        this.showJahr = false;
        this.showKlasse = false;
        this.showPerson = false;
        this.showPfad = true;
        this.showSeite = true;
        this.showStufe = false;
        this.showMediensuche = true;
        break;
      }
      case 'INTERNET': {
        this.showAusgabe = false;
        this.showJahr = true;
        this.showKlasse = true;
        this.showPerson = false;
        this.showPfad = true;
        this.showSeite = false;
        this.showStufe = true;
        this.showMediensuche = true;
        break;
      }
      case 'PERSON': {
        this.showAusgabe = false;
        this.showJahr = false;
        this.showKlasse = false;
        this.showPerson = true;
        this.showPfad = false;
        this.showSeite = false;
        this.showStufe = false;
        this.showMediensuche = false;
        break;
      }
      case 'ZEITSCHRIFT': {
        this.showAusgabe = true;
        this.showJahr = true;
        this.showKlasse = false;
        this.showPerson = false;
        this.showPfad = true;
        this.showSeite = true;
        this.showStufe = false;
        this.showMediensuche = true;
        break;
      }
    }
  }

  // ================================================================================================================
  // select controls handler
  // ================================================================================================================

  onSelectHerkunftstyp($event: string): void {
    this.selectedHerkunftstyp = $event as Herkunftstyp;
    this.quellenangabe = 'Text wird nach dem Speichern aktualisiert';

    if (this.selectedHerkunftstyp === 'EIGENKREATION') {
      this.#hideQuelle();
    }
  }

  onSelectQuellenart($event: string): void {
    this.#selectedQuellenart = $event as Quellenart;
    this.#handleQuellenartChanged();
  }

  onSelectMedium($event: MediumQuelleDto): void {
    this.#mediumUuid = $event.id;
  }

  onChangeAnzahlAntwortvorschlaege($event: Event) {

    const inputElement = $event.target as HTMLInputElement;
    const anz = parseInt(inputElement.value);
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

  #hideQuelle(): void {

    this.quelle = { ...initialQuelleDto, id: this.#autor.id };
    if (this.#autor.person) {
      this.quellenangabe = this.#autor.person;
    }
    this.showAusgabe = false;
    this.showJahr = false;
    this.showKlasse = false;
    this.showPerson = false;
    this.showPfad = false;
    this.showSeite = false;
    this.showStufe = false;
    this.showMediensuche = false;

    this.form.get('person')?.setValue('');
    this.form.get('jahr')?.setValue('');
    this.form.get('ausgabe')?.setValue('');
    this.form.get('seite')?.setValue('');
    this.form.get('klasse')?.setValue('');
    this.form.get('stufe')?.setValue('');
    this.form.get('pfad')?.setValue('');
  }


  #handleQuellenartChanged(): void {

    const quellenart: Quellenart = new GuiQuellenartenMap().getQuellenartOfLabel(this.#selectedQuellenart);
    this.searchTerm = '';

    this.#initVisibilityQuelleInputs();

    switch (quellenart) {
      case 'BUCH': {
        this.quelle.ausgabe = '';
        this.quelle.jahr = '';
        this.quelle.klasse = '';
        this.quelle.person = '';
        this.quelle.stufe = '';

        break;
      }
      case 'INTERNET': {
        this.quelle.ausgabe = '';
        this.quelle.jahr = this.quelle.jahr ? this.quelle.jahr : '';
        this.quelle.klasse = this.quelle.klasse ? this.quelle.klasse : '';
        this.quelle.person = '';
        this.quelle.seite = '';
        break;
      }
      case 'PERSON': {
        this.quelle.ausgabe = '';
        this.quelle.jahr = '';
        this.quelle.klasse = '';
        this.quelle.pfad = '';
        this.quelle.seite = '';
        this.quelle.stufe = '';
        break;
      }
      case 'ZEITSCHRIFT': {
        this.quelle.klasse = '';
        this.quelle.person = '';
        this.quelle.stufe = '';
        break;
      }
    }

    this.form.get('person')?.setValue(this.quelle.person ? this.quelle.person : '');
    this.form.get('jahr')?.setValue(this.quelle.jahr ? this.quelle.jahr : '');
    this.form.get('ausgabe')?.setValue(this.quelle.ausgabe ? this.quelle.ausgabe : '');
    this.form.get('seite')?.setValue(this.quelle.seite ? this.quelle.seite : '');
    this.form.get('klasse')?.setValue(this.quelle.klasse ? this.quelle.klasse : '');
    this.form.get('stufe')?.setValue(this.quelle.stufe ? this.quelle.stufe : '');
    this.form.get('pfad')?.setValue(this.quelle.pfad ? this.quelle.pfad : '');

    if (quellenart !== 'PERSON') {
      this.raetselFacade.findMedienForQuelle(quellenart);
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

  // ================================================================================================================
  // action handlers
  // ================================================================================================================

  cancelEdit() {
    if (this.#editRaetselPayload.id !== 'neu' && this.#editRaetselPayload.schluessel) {
      this.raetselFacade.selectRaetsel(this.#editRaetselPayload.schluessel);
    } else {
      this.raetselFacade.cancelSelection();
    }
  }

  gotoSuche(): void {
    this.raetselFacade.cancelSelection();
  }

  printPNG(): void {
    const outputformat: OutputFormat = 'PNG';
    this.#openPrintDialog(outputformat);
  }

  openHistorieLaTeXSpeichernDialog(): void {

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
      this.#doSubmit(result);
    });
  }

  submit() {

    const laTeXChanged = this.#latexChanged();
    this.#readFormValues()

    if (laTeXChanged) {
      this.openHistorieLaTeXSpeichernDialog();
    } else {
      this.#doSubmit(false);
    }
  }

  downloadLatexLogs(): void {
    if (this.#editRaetselPayload.schluessel) {
      this.raetselFacade.downloadLatexLogs(this.#editRaetselPayload.schluessel)
    }
  }

  onFileSelected($event: FileInfoModel, textart: Textart): void {
    if (textart === 'FRAGE') {
      this.fileInfoFrage = $event;
    }
    if (textart === 'LOESUNG') {
      this.fileInfoLoesung = $event;
    }

  }

  uploadFile(textart: Textart): void {

    console.log('jetzt über die EmbeddableImageFacade die action up: ' + textart);

    const context: EmbeddableImageContext = {
      raetselId: this.#editRaetselPayload.id,
      textart: textart
    };

    // im Editor wird immer nur eine neue Datei hochgeladen.
    if (textart === 'FRAGE' && this.fileInfoFrage) {
      this.#embeddableImagesFacade.createEmbeddableImage(context, this.fileInfoFrage!.file);
    }
    if (textart === 'LOESUNG' && this.fileInfoLoesung) {
      this.#embeddableImagesFacade.createEmbeddableImage(context, this.fileInfoLoesung!.file);
    }
  }


  #readFormValues(): void {
    const formValue = this.form.value;

    const antwortvorschlaegeNeu: Antwortvorschlag[] = this.#collectAntwortvorschlaege();

    // Falls undefined, dann, weil das input-Field disabled ist.
    const c_schluessel = formValue['schluessel'] ? formValue['schluessel'].trim() : this.#editRaetselPayload.schluessel;

    const theQuelle: QuelleDto = {
      id: this.quelle.id,
      ausgabe: formValue['ausgabe'] ? formValue['ausgabe'].trim() : null,
      jahr: formValue['jahr'] ? formValue['jahr'].trim() : null,
      klasse: formValue['klasse'] ? formValue['klasse'].trim() : null,
      mediumUuid: this.#mediumUuid,
      person: formValue['person'] ? formValue['person'].trim() : null,
      pfad: formValue['pfad'] ? formValue['pfad'].trim() : null,
      quellenart: new GuiQuellenartenMap().getQuellenartOfLabel(this.#selectedQuellenart),
      seite: formValue['seite'] ? formValue['seite'].trim() : null,
      stufe: formValue['stufe'] ? formValue['stufe'].trim() : null,
    }

    this.#editRaetselPayload = {
      ...this.#editRaetselPayload,
      schluessel: c_schluessel,
      name: formValue['name'] !== null ? formValue['name'].trim() : '',
      freigegeben: formValue['status'] === 'FREIGEGEBEN',
      kommentar: formValue['kommentar'] !== null ? formValue['kommentar'].trim() : null,
      frage: formValue['frage'] !== null ? formValue['frage'].trim() : '',
      loesung: formValue['loesung'] !== null ? formValue['loesung'].trim() : null,
      antwortvorschlaege: antwortvorschlaegeNeu,
      deskriptoren: this.#selectedDeskriptoren,
      herkunftstyp: this.selectedHerkunftstyp,
      quelle: theQuelle
    }
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

  #latexChanged(): boolean {

    return this.#editRaetselPayload.id !== 'neu' &&
      (this.#editRaetselPayload.frage !== this.#editRaetselPayloadCache.frage || this.#editRaetselPayload.loesung !== this.#editRaetselPayloadCache.loesung);
  }

  #doSubmit(latexHistorisieren: boolean) {

    const editRaetselPayload: EditRaetselPayload = {
      ...this.#editRaetselPayload,
      latexHistorisieren: latexHistorisieren
    };

    this.raetselFacade.saveRaetsel(editRaetselPayload);
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
      data: dialogData,
      disableClose: true
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

        this.raetselFacade.generiereRaetselOutput(this.#editRaetselPayload.id, outputformat, font, schriftgroesse, layout);
      }
    });
  }

  #openEmbeddableImageVorschauDialog(vorschau: EmbeddableImageVorschau): void {

    const hinweis = 'Die Grafik kann ersetzt werden, indem sie in der Detailansicht unter Grafiken ausgewählt und anschließend eine neue hochgeladen wird '
      + 'oder indem im Editor eine neue Grafik hochgeladen und der entsprechende \\includegraphics-Befehl durch den neu generierten ersetzt wird. ';

    const dialogData: ImageDialogModel = {
      titel: vorschau.pfad,
      hinweis: hinweis,
      image: vorschau.image
    }

    const height = vorschau.exists ? '650' : '270px';
    const width = vorschau.exists ? '700px' : '500px';

    const dialogRef = this.dialog.open(ImageDialogComponent, {
      height: height,
      width: width,
      data: dialogData,
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(() => {
      this.#embeddableImagesFacade.clearVorschau();
    });
  }
}
