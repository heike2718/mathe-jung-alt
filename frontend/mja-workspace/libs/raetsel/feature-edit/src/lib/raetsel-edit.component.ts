import { Component, OnInit } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { GrafikFacade } from '@mja-workspace/grafik/domain';
import { QuellenFacade } from '@mja-workspace/quellen/domain';
import { Antwortvorschlag,
  anzeigeAntwortvorschlaegeSelectInput,
  EditRaetselPayload,
  GrafikInfo,
  LATEX_LAYOUT_ANTWORTVORSCHLAEGE,
  RaetselDetails,
  RaetselDetailsContent,
  RaetselFacade 
} from '@mja-workspace/raetsel/domain';
import { JaNeinDialogComponent, JaNeinDialogData, SelectItemsCompomentModel } from '@mja-workspace/shared/ui-components';
import { PrintRaetselDialogComponent, PrintRaetselDialogData } from '@mja-workspace/shared/ui-components';
import { Message, SelectableItem, STATUS } from '@mja-workspace/shared/util-mja';
import { Deskriptor, Suchkontext } from '@mja-workspace/suchfilter/domain';
import { combineLatest, Subscription } from 'rxjs';

interface AntwortvorschlagFormValue {
  text: string,
  korrekt: boolean;
};

@Component({
  selector: 'mja-raetsel-edit',
  templateUrl: './raetsel-edit.component.html',
  styleUrls: ['./raetsel-edit.component.scss'],
})
export class RaetselEditComponent implements OnInit {

  // https://coryrylan.com/blog/building-reusable-forms-in-angular
  // https://stackblitz.com/edit/angular-10-dynamic-reactive-forms-example?file=src%2Fapp%2Fapp.component.html
  // https://jasonwatmore.com/post/2020/09/18/angular-10-dynamic-reactive-forms-example
  // #raetsel: RaetselDetails = initialRaetselDetails;
  raetselDetailsContent!: RaetselDetailsContent;
  selectDeskriptorenComponentModel!: SelectItemsCompomentModel;

  #kontext: Suchkontext = 'RAETSEL';


  #raetselSubscription: Subscription = new Subscription();
  #selectedQuelleSubscription: Subscription = new Subscription();

  #selectedDeskriptoren: Deskriptor[] = [];

  anzahlenAntwortvorschlaege = ['0', '2', '3', '5', '6'];

  selectStatusInput: STATUS[] = ['ERFASST', 'FREIGEGEBEN'];

  form!: UntypedFormGroup;

  constructor(public raetselFacade: RaetselFacade,
    public quellenFacade: QuellenFacade,
    private grafikFacade: GrafikFacade,
    private fb: UntypedFormBuilder,
    public dialog: MatDialog) {

    this.form = this.fb.group({
      schluessel: ['', [Validators.required, Validators.pattern('^[0-9]{5}$')]],
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

    this.#raetselSubscription = combineLatest([this.raetselFacade.editorContent$, this.raetselFacade.editorSelectableDeskriptoren$])
      .subscribe(([raetsel, items]) => {

        if (raetsel) {
          this.raetselDetailsContent = raetsel;

          const gewaehlteItems: SelectableItem[] = [];
          this.raetselDetailsContent.raetsel.deskriptoren.forEach(d => gewaehlteItems.push({ id: d.id, name: d.name, selected: true }));
          this.#selectedDeskriptoren = this.raetselDetailsContent.raetsel.deskriptoren;

          this.selectDeskriptorenComponentModel = {
            ueberschriftAuswahlliste: 'Deskriptoren',
            ueberschriftGewaehlteItems: 'gewählt:',
            vorrat: items,
            gewaehlteItems: gewaehlteItems
          };
          this.#initForm();
        }

      });

    this.#selectedQuelleSubscription = this.quellenFacade.selectedQuelle$.subscribe(
      quelle => {
        if (quelle) {
          const raetsel: RaetselDetails = { ...this.#readFormValues(), quelleId: quelle.id };
          // this.raetselFacade.cacheRaetselDetails(raetsel);
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.#raetselSubscription.unsubscribe();
    this.#selectedQuelleSubscription.unsubscribe();
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

  #doSubmit(raetsel: RaetselDetails, latexHistorisieren: boolean) {

    const editRaetselPayload: EditRaetselPayload = {
      latexHistorisieren: latexHistorisieren,
      raetsel: raetsel
    };

    this.raetselFacade.saveRaetsel(editRaetselPayload);
  }

  cancelEdit() {
    if (this.raetselDetailsContent && this.raetselDetailsContent.raetsel.id !== 'neu') {
      this.raetselFacade.selectRaetsel(this.raetselDetailsContent.raetsel);
    } else {
      this.raetselFacade.cancelEditRaetsel();
    }
  }

  isFormValid(): boolean {
    if (this.form.valid) {
      return true;
    }
    return false;
  }

  // convenience getters for easy access to form fields
  get avFormArray() { return this.form.controls['antwortvorschlaege'] as UntypedFormArray; }
  get antwortvorschlaegeFormGroup() { return this.avFormArray.controls as UntypedFormGroup[]; }

  onChangeAnzahlAntwortvorschlaege($event: any) {

    const anz = parseInt($event.target.value);
    this.#addOrRemoveAntowrtvorschlagFormParts(anz);
  }

  onSelectedDesktiptorenChanged($event: any) {

    if ($event) {
      const selectedItems: SelectableItem[] = (<SelectItemsCompomentModel>$event).gewaehlteItems;
      this.#selectedDeskriptoren = [];
      selectedItems.forEach(item => this.#selectedDeskriptoren.push(
        {
          id: item.id,
          admin: true,
          kontext: this.#kontext,
          name: item.name
        }));
    }
  }

  quelleSuchen(): void {
    // const raetsel: RaetselDetails = this.#readFormValues();
    // this.raetselFacade.cacheRaetselDetails(raetsel);
    this.quellenFacade.navigateToQuellensuche();
  }

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

    const grafikInfosOhneFile: GrafikInfo[] = this.raetselDetailsContent.raetsel.grafikInfos.filter(gi => !gi.existiert);
    return !this.form.valid || this.antwortvorschlaegeErrors() || grafikInfosOhneFile.length > 0;
  }

  openPrintPNGDialog(): void {
    const dialogData: PrintRaetselDialogData = {
      titel: 'PNG generieren',
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

        this.raetselFacade.generiereRaetselOutput(this.raetselDetailsContent.raetsel.id, 'PNG', layout);
      }
    });
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

  grafikLaden(link: string): void {
    this.grafikFacade.grafikPruefen(link);
  }

  onGrafikHochgeladen($event: Message): void {
    if ($event.level === 'INFO') {
      const pfad = $event.message;
      this.raetselFacade.grafikHochgeladen(this.raetselDetailsContent.raetsel, pfad);
    }
  }

  #initForm() {

    const raetsel = this.raetselDetailsContent.raetsel;

    this.form.controls['schluessel'].setValue(raetsel.schluessel);
    this.form.controls['name'].setValue(raetsel.name);
    this.form.controls['quelleId'].setValue(raetsel.quelleId);
    this.form.controls['status'].setValue(raetsel.status);
    this.form.controls['frage'].setValue(raetsel.frage);
    this.form.controls['loesung'].setValue(raetsel.loesung);
    this.form.controls['kommentar'].setValue(raetsel.kommentar);
    this.form.controls['anzahlAntwortvorschlaege'].setValue(raetsel.antwortvorschlaege.length + '');

    this.#addOrRemoveAntowrtvorschlagFormParts(raetsel.antwortvorschlaege.length);

    for (let i = 0; i < raetsel.antwortvorschlaege.length; i++) {

      const avGroup = this.avFormArray.at(i);
      const av: Antwortvorschlag = raetsel.antwortvorschlaege[i];

      avGroup.setValue({ text: av.text, korrekt: av.korrekt });
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

  #addOrRemoveAntowrtvorschlagFormParts(anz: number) {

    if (this.avFormArray.length < anz) {

      for (let i = this.avFormArray.length; i < anz; i++) {
        this.avFormArray.push(this.fb.group({
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

    const raetselDetails: RaetselDetails = {
      ...this.raetselDetailsContent.raetsel,
      schluessel: formValue['schluessel'].trim(),
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

  #latexChanged(raetsel: RaetselDetails): boolean {

    return this.raetselDetailsContent.raetsel.id !== 'neu' &&
      (raetsel.frage !== this.raetselDetailsContent.raetsel.frage || raetsel.loesung !== this.raetselDetailsContent.raetsel.loesung);
  }
}
