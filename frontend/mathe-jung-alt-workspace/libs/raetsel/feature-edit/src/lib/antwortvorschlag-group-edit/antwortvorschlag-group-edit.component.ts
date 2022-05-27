import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Antwortvorschlag } from '@mathe-jung-alt-workspace/raetsel/domain';
import { Subscription } from 'rxjs';

export interface AntwortvorschlaegeFormValues {
  anzahlAntwortvorschlaege: string,
  antwortvorschlaege: Antwortvorschlag[]
};

@Component({
  selector: 'mja-antwortvorschlag-group',
  templateUrl: './antwortvorschlag-group-edit.component.html',
  styleUrls: ['./antwortvorschlag-group-edit.component.scss'],
})
export class AntwortvorschlagGroupEditComponent implements OnDestroy {

  antwortvorschlaegeForm!: FormGroup;
  subscriptions: Subscription[] = [];

  constructor(private fb: FormBuilder) { 

    this.antwortvorschlaegeForm = this.fb.group({
      anzahlAntwortvorschlaege: ['', [Validators.required]],
      antwortvorschlaege: new FormArray([]),
    });   

  }
  
  ngOnDestroy(): void {

    this.subscriptions.forEach(s => s.unsubscribe());
      
  }

  get value(): AntwortvorschlaegeFormValues {
    return this.antwortvorschlaegeForm.value;
  }

  set value(value: AntwortvorschlaegeFormValues) {
    this.antwortvorschlaegeForm.setValue(value);
    this.onChange(value);
    this.onTouched(value);
  }

  onChange: any = () => { };
  onTouched: any = () => { };


  // from ControlValueAccessor
  writeValue(value: any): void {
    if (value) {
      this.value = value;
    }

    if (value === null) {
      this.antwortvorschlaegeForm.reset();
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
    return this.antwortvorschlaegeForm.valid ? null : { antwortvorschlaege: { valid: false } };
  }

  antwortvorschlagError = (controlName: string, errorName: string) => {
    return this.antwortvorschlaegeForm.controls[controlName].hasError(errorName);
  }
}
