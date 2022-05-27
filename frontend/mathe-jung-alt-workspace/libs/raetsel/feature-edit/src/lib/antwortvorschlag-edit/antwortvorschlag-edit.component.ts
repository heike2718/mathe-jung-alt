import { Component, forwardRef, Input, OnDestroy, OnInit } from '@angular/core';
import { ControlValueAccessor, FormBuilder, FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';

export interface AntwortvorschlagFormValues {
  buchstabe: string;
  text: string;
  korrekt: boolean;
};

@Component({
  selector: 'mja-antwortvorschlag-edit',
  templateUrl: './antwortvorschlag-edit.component.html',
  styleUrls: ['./antwortvorschlag-edit.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => AntwortvorschlagEditComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => AntwortvorschlagEditComponent),
      multi: true
    }
  ]
})
export class AntwortvorschlagEditComponent implements OnDestroy, ControlValueAccessor {

  antwortvorschlagForm!: FormGroup;
  subscriptions: Subscription[] = [];

  constructor(private fb: FormBuilder) {

    this.antwortvorschlagForm = this.fb.group({

      buchstabe: ['', [Validators.required, Validators.maxLength(1)]],
      text: ['', [Validators.required, Validators.maxLength(30)]],
      korrekt: [false, [Validators.required]]
    });

    this.subscriptions.push(
      // any time the inner form changes update the parent of any change
      this.antwortvorschlagForm.valueChanges.subscribe(value => {
        this.onChange(value);
        this.onTouched();
      })
    ); 
  }

  get value(): AntwortvorschlagFormValues {
    return this.antwortvorschlagForm.value;
  }

  set value(value: AntwortvorschlagFormValues) {
    this.antwortvorschlagForm.setValue(value);
    this.onChange(value);
    this.onTouched(value);
  }

  getBuchstabeControl() {
    return this.antwortvorschlagForm.controls.buchstabe;
  }

  getTextControl() {
    return this.antwortvorschlagForm.controls.text;
  }

  getKorrektContro() {
    return this.antwortvorschlagForm.controls.korrekt;
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
      this.antwortvorschlagForm.reset();
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
    return this.antwortvorschlagForm.valid ? null : { antwortvorschlag: { valid: false } };
  }

  antwortvorschlagError = (controlName: string, errorName: string) => {
    return this.antwortvorschlagForm.controls[controlName].hasError(errorName);
  }

}
