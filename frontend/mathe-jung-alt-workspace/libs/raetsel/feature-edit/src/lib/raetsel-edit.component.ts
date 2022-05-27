import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder, FormGroup
} from '@angular/forms';
import { Antwortvorschlag, initialRaetselDetails, RaetselDetails, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { Subscription } from 'rxjs';
import { RaetselDataFormValues } from './raetsel-data-edit/raetsel-data-edit.component';

@Component({
  selector: 'raetsel-raetsel-feature-edit',
  templateUrl: './raetsel-edit.component.html',
  styleUrls: ['./raetsel-edit.component.scss']
})
export class RaetselEditComponent implements OnInit, OnDestroy {

  // https://coryrylan.com/blog/building-reusable-forms-in-angular
  #raetsel: RaetselDetails = initialRaetselDetails;
  #raetselSubscription: Subscription = new Subscription();

  form!: FormGroup;

  constructor(public raetselFacade: RaetselFacade, private fb: FormBuilder) {
    this.form = this.fb.group({
      raetselData: [],
      antwortvorschlag: [],
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

     
  }

  ngOnDestroy(): void {
    this.#raetselSubscription.unsubscribe();
  }

  submit() {
    console.log(JSON.stringify(this.form.value));
    // console.log(JSON.stringify(this.#raetsel));
  }

  private initForm() {

    const raetselDataFormValue: RaetselDataFormValues = {
      schluessel: this.#raetsel.schluessel,
      name: this.#raetsel.name,
      quelleId: this.#raetsel.quelleId,
      frage: this.#raetsel.frage,
      loesung: this.#raetsel.loesung,
      kommentar: this.#raetsel.kommentar
    };

    this.form.controls.raetselData.setValue(raetselDataFormValue);

    const antwortvorschlag: Antwortvorschlag = this.#raetsel.antwortvorschlaege[0];
    this.form.controls.antwortvorschlag.setValue({ buchstabe: antwortvorschlag.buchstabe, text: antwortvorschlag.text, korrekt: antwortvorschlag.korrekt });
  }
}
