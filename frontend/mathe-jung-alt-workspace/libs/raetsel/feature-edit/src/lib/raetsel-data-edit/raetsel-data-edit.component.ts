import { Component, forwardRef, OnDestroy, OnInit } from '@angular/core';
import { ControlValueAccessor, FormBuilder, FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';

export interface RaetselDataFormValues {
  schluessel: string;
  name: string;
  quelleId: string;
  frage: string;
  loesung?: string;
  kommentar?: string;
};

@Component({
  selector: 'mja-raetsel-data-edit',
  templateUrl: './raetsel-data-edit.component.html',
  styleUrls: ['./raetsel-data-edit.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RaetselDataEditComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => RaetselDataEditComponent),
      multi: true
    }
  ]
})
export class RaetselDataEditComponent implements OnDestroy, ControlValueAccessor {

  raetselDataForm!: FormGroup;
  subscriptions: Subscription[] = [];

  constructor(private fb: FormBuilder) { 

    this.raetselDataForm = this.fb.group({

      schluessel: ['', [Validators.required, Validators.pattern('^[0-9]{5}$')]],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      quelleId: ['', [Validators.required]],
      frage: ['', [Validators.required]],
      loesung: [''],
      kommentar: ['']
    });

    this.subscriptions.push(
      // any time the inner form changes update the parent of any change
      this.raetselDataForm.valueChanges.subscribe(value => {
        this.onChange(value);
        this.onTouched();
      })
    );
  }

  get value(): RaetselDataFormValues {
    return this.raetselDataForm.value;
  }

  set value(value: RaetselDataFormValues) {
    this.raetselDataForm.setValue(value);
    this.onChange(value);
    this.onTouched(value);
  }

  getSchluesselControl() {
    return this.raetselDataForm.controls.schluessel;
  }

  getNameControl() {
    return this.raetselDataForm.controls.name;
  }

  getQuelleIdControl() {
    return this.raetselDataForm.controls.quelleId;
  }

  getFrageControl() {
    return this.raetselDataForm.controls.frage;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  onChange: any = () => { };
  onTouched: any = () => { };

  // from ControlValueAccessor
  writeValue(value: any): void {
    if (value) {
      this.value = value;
    }

    if (value === null) {
      this.raetselDataForm.reset();
    }
  }

  // from ControlValueAccessor
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  // from ControlValueAccessor
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  // communicate the inner form validation to the parent form
  validate(_: FormControl) {
    return this.raetselDataForm.valid ? null : { raetselData: { valid: false } };
  }

  raetselDataError = (controlName: string, errorName: string) => {
    return this.raetselDataForm.controls[controlName].hasError(errorName);
  }
}
