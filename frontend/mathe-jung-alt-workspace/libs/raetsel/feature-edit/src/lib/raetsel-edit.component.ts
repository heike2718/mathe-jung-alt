import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { initialRaetselDetails, RaetselDetails, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { Subscription } from 'rxjs';

@Component({
  selector: 'raetsel-raetsel-feature-edit',
  templateUrl: './raetsel-edit.component.html',
  styleUrls: ['./raetsel-edit.component.scss'],
})
export class RaetselEditComponent implements OnInit, OnDestroy {

  #formChangesSubscription: Subscription = new Subscription();
  #raetsel: RaetselDetails = initialRaetselDetails;

  form!: FormGroup;

  constructor(public raetselFacade: RaetselFacade, private fb: FormBuilder) { }

  ngOnInit(): void {

    this.form = this.fb.group({

      schluessel: [null, [Validators.required, Validators.pattern('^[0-9]{5}$')]],
      name: [null, [Validators.required, Validators.maxLength(100)]],
      quelleId: [null, [Validators.required]],
      frage: [null, [Validators.required]],
      loesung: [null],
      kommentar: [null]
    });

    this.form.valueChanges.subscribe(val => {

      this.#raetsel = { ...this.#raetsel, 
        schluessel: val['schluessel'],
        name: val['name'],
        kommentar: val['kommentar'],  };
    });
  }

  ngOnDestroy(): void {
    this.#formChangesSubscription.unsubscribe();
  }

  hasValidationErrors(): boolean {
    return !this.form.valid;
  }

  submit() {
    console.log(JSON.stringify(this.#raetsel));
    if (this.hasValidationErrors()) {
      return;
    }    
  }

  public myError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }
}
