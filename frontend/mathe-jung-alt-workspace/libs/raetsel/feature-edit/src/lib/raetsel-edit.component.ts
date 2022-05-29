import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder, FormControl, FormGroup, Validators
} from '@angular/forms';
import { Deskriptor, DeskriptorenSearchFacade } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { Antwortvorschlag, EditRaetselPayload, initialRaetselDetails, Raetsel, RaetselDetails, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { filter, Subscription } from 'rxjs';

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
  #raetsel: RaetselDetails = initialRaetselDetails;

  #selectedDeskriptoren: Deskriptor[] = [];


  #raetselSubscription = new Subscription();
  #deskriptorenLoadedSubscripion = new Subscription();
  #deskriptorenSubscription: Subscription = new Subscription();

  anzahlenAntwortvorschlaege = ['0', '2', '3', '5', '6'];

  form!: FormGroup;

  constructor(public raetselFacade: RaetselFacade,
    private deskriptorenSearchFacade: DeskriptorenSearchFacade,
    public quellenFacade: QuellenFacade,
    private fb: FormBuilder) {

    this.form = this.fb.group({
      schluessel: ['', [Validators.required, Validators.pattern('^[0-9]{5}$')]],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      quelleId: ['', [Validators.required]],
      frage: ['', [Validators.required]],
      loesung: [''],
      kommentar: [''],
      anzahlAntwortvorschlaege: ['0'],
      antwortvorschlaege: new FormArray([])
    });
  }

  ngOnInit(): void {

    this.#raetselSubscription = this.raetselFacade.raetselDetails$.subscribe(
      raetsel => {
        if (raetsel) {
          this.#raetsel = raetsel;
          this.initForm();
        }
      }
    );

    this.#deskriptorenLoadedSubscripion = this.deskriptorenSearchFacade.loaded$.pipe(
      filter(loaded => loaded)
    ).subscribe(
      () => {
        if (this.#raetsel) {
          this.#raetsel.deskriptoren.forEach(deskriptor => this.deskriptorenSearchFacade.addToSearchlist(deskriptor));
        }
      }
    );

    this.#deskriptorenSubscription = this.deskriptorenSearchFacade.suchliste$.subscribe(
      suchliste => this.#selectedDeskriptoren = suchliste
    );
  }

  ngOnDestroy(): void {
    this.#raetselSubscription.unsubscribe();
    this.#deskriptorenLoadedSubscripion.unsubscribe();
    this.#deskriptorenSubscription.unsubscribe();
  }

  submit() {

    const formValue = this.form.value;

    console.log(JSON.stringify(this.form.value));

    const antwortvorschlaegeNeu: Antwortvorschlag[] = this.collectAntwortvorschlaege();

    const frageNeu = formValue['frage'].trim();
    const loesungNeu = formValue['loesung'].trim();

    const latexHistorisieren = frageNeu !== this.#raetsel.frage || loesungNeu !== this.#raetsel.loesung;

    const raetsel: RaetselDetails = {
      ...this.#raetsel,
      schluessel: formValue['schluessel'].trim(),
      name: formValue['name'].trim(),
      kommentar: formValue['kommentar'].trim() === '' ? null : formValue['kommentar'],
      frage: formValue['frage'].trim(),
      loesung: formValue['loesung'].trim() === '' ? null : formValue['loesung'].trim(),
      antwortvorschlaege: antwortvorschlaegeNeu,
      deskriptoren: this.#selectedDeskriptoren,
      imageFrage: null,
      imageLoesung: null
    };

    const editRaetselPayload: EditRaetselPayload = {
      latexHistorisieren: latexHistorisieren,
      raetsel: raetsel
    };

    this.raetselFacade.saveRaetsel(editRaetselPayload);
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

  onChangeAnzahlAntwortvorschlaege(e: any) {

    const anz = parseInt(e.target.value);
    this.addOrRemoveAntowrtvorschlagFormParts(anz);
  }

  onDeskriptorenChanged(_$event: Deskriptor[]): void {
    // wird über die #deskriptorenSubscription bereits erledigt
  }

  raetselDataError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }

  antwortvorschlaegeErrors() : boolean {
    const antworten: Antwortvorschlag[] = this.collectAntwortvorschlaege();

    let anzahlKorrekt = 0;
    
    antworten.forEach( a => { 
      if (a.korrekt){
        anzahlKorrekt++
      }
    });

    return anzahlKorrekt !== 1;
  }

  private initForm() {

    this.form.controls.schluessel.setValue(this.#raetsel.schluessel);
    this.form.controls.name.setValue(this.#raetsel.name);
    this.form.controls.quelleId.setValue(this.#raetsel.quelleId);
    this.form.controls.frage.setValue(this.#raetsel.frage);
    this.form.controls.loesung.setValue(this.#raetsel.loesung);
    this.form.controls.kommentar.setValue(this.#raetsel.kommentar);
    this.form.controls.anzahlAntwortvorschlaege.setValue(this.#raetsel.antwortvorschlaege.length + '');

    this.addOrRemoveAntowrtvorschlagFormParts(this.#raetsel.antwortvorschlaege.length);

    for (let i = 0; i < this.#raetsel.antwortvorschlaege.length; i++) {

      const avGroup = this.avFormArray.at(i);
      const av: Antwortvorschlag = this.#raetsel.antwortvorschlaege[i];

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

  private getIndex(buchstabe: string): number {

    if ('A' === buchstabe) {
      return 0;
    }
    if ('B' === buchstabe) {
      return 1;
    }
    if ('C' === buchstabe) {
      return 2;
    }
    if ('D' === buchstabe) {
      return 3;
    }
    if ('E' === buchstabe) {
      return 4;
    }
    if ('F' === buchstabe) {
      return 5;
    }

    return -1;
  }
}
