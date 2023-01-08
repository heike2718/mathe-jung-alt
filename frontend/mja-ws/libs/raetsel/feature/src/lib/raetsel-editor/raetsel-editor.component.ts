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
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { TextFieldModule } from '@angular/cdk/text-field';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatExpansionModule } from '@angular/material/expansion';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { Antwortvorschlag, GrafikInfo, RaetselDetails } from '@mja-ws/raetsel/model';
import { Subscription, tap } from 'rxjs';
import { ReactiveFormsModule, UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { DeskriptorUI, STATUS } from '@mja-ws/core/model';
import { FrageLoesungImagesComponent } from '@mja-ws/shared/components';

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
    MatChipsModule,
    MatDialogModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatListModule,
    MatSlideToggleModule,
    CdkAccordionModule,
    TextFieldModule,
    FlexLayoutModule,
    MatExpansionModule,
    ReactiveFormsModule,
    FrageLoesungImagesComponent
  ],
  templateUrl: './raetsel-editor.component.html',
  styleUrls: ['./raetsel-editor.component.scss'],
})
export class RaetselEditorComponent implements OnInit, OnDestroy {


  #raetselDetails!: RaetselDetails;
  #fb = inject(UntypedFormBuilder);

  #raetselDetailsSubscription = new Subscription();

  #selectedDeskriptoren: DeskriptorUI[] = [];

  anzahlenAntwortvorschlaege = ['0', '2', '3', '5', '6'];

  selectStatusInput: STATUS[] = ['ERFASST', 'FREIGEGEBEN'];

  raetselFacade = inject(RaetselFacade);
  form!: UntypedFormGroup;

  constructor() {

    this.form = this.#fb.group({
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
    this.#raetselDetailsSubscription = this.raetselFacade.raetselDetails$.pipe(
      tap((details) => {
        this.#raetselDetails = details;
        this.#initForm();
      })
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.#raetselDetailsSubscription.unsubscribe();
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

    const raetselDetails: RaetselDetails = {
      ...this.#raetselDetails,
      schluessel: formValue['schluessel'].trim(),
      name: formValue['name'] !== null ? formValue['name'].trim() : '',
      status: formValue['status'],
      kommentar: formValue['kommentar'] !== null ? formValue['kommentar'].trim() : null,
      frage: formValue['frage'] !== null ? formValue['frage'].trim() : '',
      loesung: formValue['loesung'] !== null ? formValue['loesung'].trim() : null,
      antwortvorschlaege: antwortvorschlaegeNeu,
      // deskriptoren: this.#selectedDeskriptoren,
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
}
