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
import {
  Antwortvorschlag,
  EditRaetselPayload,
  GUIEditRaetselPayload,
  GuiHerkunfsttyp,
  GuiHerkunftstypenMap,
  GuiQuellenart,
  GuiQuellenartenMap,
  MediumQuelleDto
} from '@mja-ws/raetsel/model';
import { combineLatest, Subscription } from 'rxjs';
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
import { Router } from '@angular/router';

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

  selectQuellenartInput: string[] = new GuiQuellenartenMap().getLabelsSorted();
  #selectedQuellenart: string = '';

  selectedMedium: MediumQuelleDto | undefined;
  medienForQuelle: MediumQuelleDto[] = [];

  showMediensuche = false;
  showKlasse = false;
  showStufe = false;
  showAusgabe = false;
  showJahr = false;
  showSeite = false;
  showPerson = false;
  showPfad = false;


  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //   RAETSELTEIL
  // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  #router = inject(Router);

  guiEditRaetselPayload!: GUIEditRaetselPayload;

  #infoIncludegraphics = 'Nach dem Hochladen erscheint der LaTeX-Befehl zum Einbinden der Grafik am Ende des Textes und kann an eine beliebiege Stelle verschoben werden. Das width-Attribut kann geändert werden. Um eine eingebundene Grafik wieder zu löschen, bitte einfach den \\includegraphics-Befehl aus dem Text entfernen und speichern. Dabei wird auf dem Server auch die Grafikdatei gelöscht.';
  #warnungIncludegraphics = 'Bitte den \\includegraphics-Befehl nicht manuell einfügen. Er wird beim Hochladen einer neuen Datei vom System generiert. Der generierte Pfad darf nicht geändert werden!';

  #fb = inject(UntypedFormBuilder);

  #coreFacade = inject(CoreFacade);
  #authFacade = inject(AuthFacade);
  #embeddableImagesFacade = inject(EmbeddableImagesFacade);

  #selectedDeskriptoren: DeskriptorUI[] = [];
  selectedHerkunftstyp!: Herkunftstyp;

  isRoot = false;

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

  #combinedSubscription = new Subscription();
  #embeddableImagesResponseSubscription: Subscription = new Subscription();
  #combinedEmbeddableImageVorschauSubscription: Subscription = new Subscription();
  #medienForQuelleSubscription: Subscription = new Subscription();


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
      seite: ['', [Validators.maxLength(10)]],
      klasse: ['', [Validators.maxLength(20)]],
      stufe: ['', [Validators.maxLength(10)]],
      pfad: ['', [Validators.maxLength(255)]],
    });

  }

  ngOnInit(): void {

    this.#combinedSubscription = combineLatest([this.raetselFacade.editRaetselPayload$,
    this.#coreFacade.alleDeskriptoren$,
    this.#authFacade.userIsRoot$
    ])
      .subscribe(([guiEditRaetselPayload,
        alleDeskriptoren,
        root
      ]) => {

        this.guiEditRaetselPayload = guiEditRaetselPayload;
        this.selectedHerkunftstyp = this.guiEditRaetselPayload.editRaetselPayload.herkunftstyp;
        this.#selectedQuellenart = new GuiQuellenartenMap().getLabelOfQuellenart(this.guiEditRaetselPayload.editRaetselPayload.quelle.quellenart);

        this.#editRaetselPayloadCache = { ...this.guiEditRaetselPayload.editRaetselPayload };
        this.#selectedDeskriptoren = this.guiEditRaetselPayload.editRaetselPayload.deskriptoren;
        this.selectItemsCompomentModel = this.raetselFacade.initSelectItemsCompomentModel(this.guiEditRaetselPayload.editRaetselPayload.deskriptoren, alleDeskriptoren);
        this.embeddableImageInfosFrage = guiEditRaetselPayload.embeddableImageInfos.filter((info) => info.textart === 'FRAGE');
        this.embeddableImageInfosLoesung = guiEditRaetselPayload.embeddableImageInfos.filter((info) => info.textart === 'LOESUNG');

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


    this.#medienForQuelleSubscription = combineLatest([this.raetselFacade.editRaetselPayload$,
    this.raetselFacade.medienForQuelle$]).subscribe(([payload, medien]) => {

      this.medienForQuelle = medien;
      if (payload.editRaetselPayload.quelle.mediumUuid) {
        const filtered: MediumQuelleDto[] = medien.filter(m => m.id === this.guiEditRaetselPayload.editRaetselPayload.quelle.mediumUuid);
        if (filtered.length > 0) {
          this.selectedMedium = filtered[0];
          this.form.get('medium')?.patchValue(this.selectedMedium);
        } else {
          this.selectedMedium = undefined;
        }
      }
    });
  }

  ngOnDestroy(): void {
    this.#combinedSubscription.unsubscribe();
    this.#embeddableImagesResponseSubscription.unsubscribe();
    this.#combinedEmbeddableImageVorschauSubscription.unsubscribe();
    this.#embeddableImagesFacade.clearVorschau();
    this.#medienForQuelleSubscription.unsubscribe();
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

    const theStatus = this.guiEditRaetselPayload.editRaetselPayload.freigegeben ? 'FREIGEGEBEN' : 'ERFASST';

    const theGuiHerkunftstyp: GuiHerkunfsttyp = new GuiHerkunftstypenMap().getGuiHerkunftstyp(this.guiEditRaetselPayload.editRaetselPayload.herkunftstyp);
    const theGuiQuellenart: GuiQuellenart = new GuiQuellenartenMap().getGuiQuellenart(this.guiEditRaetselPayload.editRaetselPayload.quelle.quellenart);

    this.form.get('schluessel')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.schluessel);
    this.form.get('name')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.name);
    this.form.get('herkunftstyp')?.setValue(theGuiHerkunftstyp.label);
    this.form.get('status')?.setValue(theStatus);
    this.form.get('frage')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.frage);
    this.form.get('loesung')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.loesung);
    this.form.get('kommentar')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.kommentar);
    this.form.get('quellenart')?.setValue(theGuiQuellenart.label);

    this.form.get('person')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.quelle.person ? this.guiEditRaetselPayload.editRaetselPayload.quelle.person : '');
    this.form.get('jahr')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.quelle.jahr ? this.guiEditRaetselPayload.editRaetselPayload.quelle.jahr : '');
    this.form.get('ausgabe')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.quelle.ausgabe ? this.guiEditRaetselPayload.editRaetselPayload.quelle.ausgabe : '');
    this.form.get('seite')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.quelle.seite ? this.guiEditRaetselPayload.editRaetselPayload.quelle.seite : '');
    this.form.get('klasse')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.quelle.klasse ? this.guiEditRaetselPayload.editRaetselPayload.quelle.klasse : '');
    this.form.get('stufe')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.quelle.stufe ? this.guiEditRaetselPayload.editRaetselPayload.quelle.stufe : '');
    this.form.get('pfad')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.quelle.pfad ? this.guiEditRaetselPayload.editRaetselPayload.quelle.pfad : '');

    this.form.get('anzahlAntwortvorschlaege')?.setValue(this.guiEditRaetselPayload.editRaetselPayload.antwortvorschlaege.length + '');

    this.#addOrRemoveAntowrtvorschlagFormParts(this.guiEditRaetselPayload.editRaetselPayload.antwortvorschlaege.length);

    if (!this.isRoot) {
      this.form.get('schluessel')?.disable();
    }

    for (let i = 0; i < this.guiEditRaetselPayload.editRaetselPayload.antwortvorschlaege.length; i++) {

      const avGroup = this.avFormArray.at(i);
      const av: Antwortvorschlag = this.guiEditRaetselPayload.editRaetselPayload.antwortvorschlaege[i];

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
    this.selectedHerkunftstyp = new GuiHerkunftstypenMap().getHerkunftstypOfLabel($event);

    this.guiEditRaetselPayload = {
      ...this.guiEditRaetselPayload,
      quellenangabe: 'Text wird nach dem Speichern aktualisiert.',
    }

    if (this.selectedHerkunftstyp === 'EIGENKREATION') {
      this.#hideQuelle();
    }
  }

  onSelectQuellenart($event: string): void {
    this.#selectedQuellenart = $event as Quellenart;
    this.#handleQuellenartChanged();
  }

  onSelectMedium($event: MediumQuelleDto): void {
    this.selectedMedium = $event;
  }

  compareMedia(medium1: MediumQuelleDto, medium2: MediumQuelleDto): boolean {
    // console.log('[medium1=' + (medium1 ? medium1.titel : 'undefined') + ', medium2=' + (medium2 ? medium2.titel : 'undefined') + ']');
    return medium1 && medium2 && medium1.id === medium2.id;
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

    this.guiEditRaetselPayload = {
      ...this.guiEditRaetselPayload,
      quellenangabe: 'Text wird nach dem Speichern aktualisiert.',
      editRaetselPayload: {
        ...this.guiEditRaetselPayload.editRaetselPayload
      }
    };

    this.showAusgabe = false;
    this.showJahr = false;
    this.showKlasse = false;
    this.showPerson = false;
    this.showPfad = false;
    this.showSeite = false;
    this.showStufe = false;
    this.showMediensuche = false;
  }


  #handleQuellenartChanged(): void {

    if (this.guiEditRaetselPayload.editRaetselPayload.quelle.mediumUuid) {
      const filtered: MediumQuelleDto[] = this.medienForQuelle.filter(m => m.id === this.guiEditRaetselPayload.editRaetselPayload.quelle.mediumUuid);
      if (filtered.length > 0) {
        this.selectedMedium = filtered[0];
      }
    }

    const quellenart: Quellenart = new GuiQuellenartenMap().getQuellenartOfLabel(this.#selectedQuellenart);
    const quelle: QuelleDto = {
      ... this.guiEditRaetselPayload.editRaetselPayload.quelle,
      quellenart: quellenart
    }
    this.#setFormAndModelQuelle(quelle);
  }  

  #setFormAndModelQuelle(quelle: QuelleDto): void {
    this.#initVisibilityQuelleInputs();

    switch (quelle.quellenart) {
      case 'BUCH': {
        quelle.ausgabe = '';
        quelle.jahr = '';
        quelle.klasse = '';
        quelle.person = '';
        quelle.stufe = '';

        break;
      }
      case 'INTERNET': {
        quelle.ausgabe = '';
        quelle.jahr = quelle.jahr ? quelle.jahr : '';
        quelle.klasse = quelle.klasse ? quelle.klasse : '';
        quelle.person = '';
        quelle.seite = '';
        break;
      }
      case 'PERSON': {
        quelle.ausgabe = '';
        quelle.jahr = '';
        quelle.klasse = '';
        quelle.pfad = '';
        quelle.seite = '';
        quelle.stufe = '';
        break;
      }
      case 'ZEITSCHRIFT': {
        quelle.klasse = '';
        quelle.person = '';
        quelle.stufe = '';
        break;
      }
    }

    this.guiEditRaetselPayload = {
      ... this.guiEditRaetselPayload,
      editRaetselPayload: {
        ... this.guiEditRaetselPayload.editRaetselPayload,
        quelle: quelle
      }
    };

    this.form.get('person')?.setValue(quelle.person ? quelle.person : '');
    this.form.get('jahr')?.setValue(quelle.jahr ? quelle.jahr : '');
    this.form.get('ausgabe')?.setValue(quelle.ausgabe ? quelle.ausgabe : '');
    this.form.get('seite')?.setValue(quelle.seite ? quelle.seite : '');
    this.form.get('klasse')?.setValue(quelle.klasse ? quelle.klasse : '');
    this.form.get('stufe')?.setValue(quelle.stufe ? quelle.stufe : '');
    this.form.get('pfad')?.setValue(quelle.pfad ? quelle.pfad : '');

    if (quelle.quellenart !== 'PERSON') {
      this.raetselFacade.findMedienForQuelle(quelle.quellenart);
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
    if (this.guiEditRaetselPayload.editRaetselPayload.id !== 'neu' && this.guiEditRaetselPayload.editRaetselPayload.schluessel) {
      this.raetselFacade.selectRaetsel(this.guiEditRaetselPayload.editRaetselPayload.schluessel);
    } else {
      this.raetselFacade.cancelSelection();
    }
  }

  gotoSuche(): void {
    this.raetselFacade.cancelSelection();
  }

  gotoRaetselUebersicht(): void {
    this.#router.navigateByUrl('/raetsel');
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
    if (this.guiEditRaetselPayload.editRaetselPayload.schluessel) {
      this.raetselFacade.downloadLatexLogs(this.guiEditRaetselPayload.editRaetselPayload.schluessel)
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
      raetselId: this.guiEditRaetselPayload.editRaetselPayload.id,
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
    const c_schluessel = formValue['schluessel'] ? formValue['schluessel'].trim() : this.guiEditRaetselPayload.editRaetselPayload.schluessel;
    const theQuelle: QuelleDto = {
      ... this.guiEditRaetselPayload.editRaetselPayload.quelle,
      ausgabe: formValue['ausgabe'] ? formValue['ausgabe'].trim() : null,
      jahr: formValue['jahr'] ? formValue['jahr'].trim() : null,
      klasse: formValue['klasse'] ? formValue['klasse'].trim() : null,
      mediumUuid: this.selectedMedium?.id,
      person: formValue['person'] ? formValue['person'].trim() : null,
      pfad: formValue['pfad'] ? formValue['pfad'].trim() : null,
      quellenart: new GuiQuellenartenMap().getQuellenartOfLabel(this.#selectedQuellenart),
      seite: formValue['seite'] ? formValue['seite'].trim() : null,
      stufe: formValue['stufe'] ? formValue['stufe'].trim() : null,
    }

    const theEditRaetselPayload: EditRaetselPayload = {
      ... this.guiEditRaetselPayload.editRaetselPayload,
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

    this.guiEditRaetselPayload = {
      ... this.guiEditRaetselPayload,
      editRaetselPayload: theEditRaetselPayload
    };
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

    if (this.guiEditRaetselPayload.editRaetselPayload.id === 'neu') {
      return false;
    }

    const formValue = this.form.value;

    const frageActual = formValue['frage'] !== null ? formValue['frage'].trim() : '';
    const loesungActual = formValue['loesung'] !== null ? formValue['loesung'].trim() : '';
    const result = frageActual !== this.#editRaetselPayloadCache.frage || loesungActual !== this.#editRaetselPayloadCache.loesung;

    return result;
  }

  #doSubmit(latexHistorisieren: boolean) {

    const editRaetselPayload: EditRaetselPayload = {
      ...this.guiEditRaetselPayload.editRaetselPayload,
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

        this.raetselFacade.generiereRaetselOutput(this.guiEditRaetselPayload.editRaetselPayload.id, outputformat, font, schriftgroesse, layout);
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
