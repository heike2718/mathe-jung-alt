import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder, FormGroup, Validators
} from '@angular/forms';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { Antwortvorschlag, EditRaetselPayload, RaetselDetails, RaetselEditorContent, RaetselFacade, STATUS } from '@mathe-jung-alt-workspace/raetsel/domain';
import { SelectableItem } from 'libs/shared/ui-components/src/lib/select-items/select-items.model';
import { Subscription } from 'rxjs';

interface AntwortvorschlagFormValue {
  text: string,
  korrekt: boolean;
};

@Component({
  selector: 'raetsel-raetsel-feature-edit',
  templateUrl: './raetsel-edit.component.html',
  styleUrls: ['./raetsel-edit.component.scss']
})
export class RaetselEditComponent implements OnInit, OnDestroy {

  // https://coryrylan.com/blog/building-reusable-forms-in-angular
  // https://stackblitz.com/edit/angular-10-dynamic-reactive-forms-example?file=src%2Fapp%2Fapp.component.html
  // https://jasonwatmore.com/post/2020/09/18/angular-10-dynamic-reactive-forms-example
  // #raetsel: RaetselDetails = initialRaetselDetails;
  raetselEditorContent!: RaetselEditorContent;
  #selectedDeskriptoren: Deskriptor[] = [];

  #raetselSubscription: Subscription = new Subscription();
  #selectedQuelleSubscription: Subscription = new Subscription();
  anzahlenAntwortvorschlaege = ['0', '2', '3', '5', '6'];

  selectStatusInput: STATUS[] = ['ERFASST', 'FREIGEGEBEN'];

  form!: FormGroup;

  constructor(public raetselFacade: RaetselFacade,
    public quellenFacade: QuellenFacade,
    private fb: FormBuilder) {

    this.form = this.fb.group({
      schluessel: ['', [Validators.required, Validators.pattern('^[0-9]{5}$')]],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      quelleId: ['', [Validators.required]],
      status: ['ERFASST', [Validators.required]],
      frage: ['', [Validators.required]],
      loesung: [''],
      kommentar: [''],
      anzahlAntwortvorschlaege: ['0'],
      antwortvorschlaege: new FormArray([])
    });
  }

  ngOnInit(): void {

    this.#raetselSubscription = this.raetselFacade.editorContent$.subscribe((raetsel) => {
      if (raetsel) {
        this.raetselEditorContent = raetsel;
        this.#selectedDeskriptoren = this.raetselEditorContent.raetsel.deskriptoren;
        this.initForm();
      }
    });

    this.#selectedQuelleSubscription = this.quellenFacade.selectedQuelle$.subscribe(
      quelle => {
        if (quelle) {
          const raetsel: RaetselDetails = { ...this.readFormValues(), quelleId: quelle.id };
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

    const raetsel: RaetselDetails = this.readFormValues();

    const latexHistorisieren = this.raetselEditorContent.raetsel.id !== 'neu' &&
      (raetsel.frage !== this.raetselEditorContent.raetsel.frage || raetsel.loesung !== this.raetselEditorContent.raetsel.loesung);

    const editRaetselPayload: EditRaetselPayload = {
      latexHistorisieren: latexHistorisieren,
      raetsel: raetsel
    };

    this.raetselFacade.saveRaetsel(editRaetselPayload);
  }

  cancelEdit() {
    if (this.raetselEditorContent) {
      this.raetselFacade.selectRaetsel(this.raetselEditorContent.raetsel);
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
  get avFormArray() { return this.form.controls.antwortvorschlaege as FormArray; }
  get antwortvorschlaegeFormGroup() { return this.avFormArray.controls as FormGroup[]; }

  onChangeAnzahlAntwortvorschlaege($event: any) {

    const anz = parseInt($event.target.value);
    this.addOrRemoveAntowrtvorschlagFormParts(anz);
  }

  onSelectableItemsChanged($items: SelectableItem[]) {

    this.#selectedDeskriptoren = [];

    $items.forEach(item => {
      this.#selectedDeskriptoren.push({ id: item.id, name: item.name, admin: true, kontext: 'RAETSEL' });
    });
  }

  quelleSuchen(): void {
    const raetsel: RaetselDetails = this.readFormValues();
    // this.raetselFacade.cacheRaetselDetails(raetsel);
    this.quellenFacade.navigateToQuellensuche();
  }

  raetselDataError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }

  antwortvorschlaegeErrors(): boolean {
    const antworten: Antwortvorschlag[] = this.collectAntwortvorschlaege();

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

  private initForm() {

    const raetsel = this.raetselEditorContent.raetsel;

    this.form.controls.schluessel.setValue(raetsel.schluessel);
    this.form.controls.name.setValue(raetsel.name);
    this.form.controls.quelleId.setValue(raetsel.quelleId);
    this.form.controls.frage.setValue(raetsel.frage);
    this.form.controls.loesung.setValue(raetsel.loesung);
    this.form.controls.kommentar.setValue(raetsel.kommentar);
    this.form.controls.anzahlAntwortvorschlaege.setValue(raetsel.antwortvorschlaege.length + '');

    this.addOrRemoveAntowrtvorschlagFormParts(raetsel.antwortvorschlaege.length);

    for (let i = 0; i < raetsel.antwortvorschlaege.length; i++) {

      const avGroup = this.avFormArray.at(i);
      const av: Antwortvorschlag = raetsel.antwortvorschlaege[i];

      avGroup.setValue({ text: av.text, korrekt: av.korrekt });
    }
  }

  private collectAntwortvorschlaege(): Antwortvorschlag[] {

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

  private addOrRemoveAntowrtvorschlagFormParts(anz: number) {

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

  private readFormValues(): RaetselDetails {
    const formValue = this.form.value;

    const antwortvorschlaegeNeu: Antwortvorschlag[] = this.collectAntwortvorschlaege();

    const raetsel: RaetselDetails = {
      ...this.raetselEditorContent.raetsel,
      schluessel: formValue['schluessel'].trim(),
      name: formValue['name'] !== null ? formValue['name'].trim() : '',
      status: formValue['status'],
      kommentar: formValue['kommentar'] !== null ? formValue['kommentar'].trim() : null,
      frage: formValue['frage'] !== null ? formValue['frage'].trim() : '',
      loesung: formValue['loesung'] !== null ? formValue['loesung'].trim() : null,
      antwortvorschlaege: antwortvorschlaegeNeu,
      deskriptoren: this.#selectedDeskriptoren,
      imageFrage: null,
      imageLoesung: null
    };

    return raetsel;
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
}
