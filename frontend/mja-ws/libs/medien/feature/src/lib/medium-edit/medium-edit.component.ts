import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TextFieldModule } from '@angular/cdk/text-field';
import { MedienFacade } from '@mja-ws/medien/api';
import { GuiMedienart, GuiMedienartenMap, Medienart, initialGuiMedienart } from '@mja-ws/core/model';
import { MediumDto } from '@mja-ws/medien/model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-medium-edit',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatGridListModule,
    MatInputModule,
    MatFormFieldModule,
    MatListModule,
    FormsModule,
    TextFieldModule,
    ReactiveFormsModule
  ],
  templateUrl: './medium-edit.component.html',
  styleUrl: './medium-edit.component.scss',
})
export class MediumEditComponent implements OnInit, OnDestroy {

  medienFacade = inject(MedienFacade);
  selectStatusInput: string[] = ['ERFASST', 'FREIGEGEBEN'];
  selectMedienartenInput: string[] = new GuiMedienartenMap().getLabelsSorted();

  form!: FormGroup;
  medium!: MediumDto;

  #formBuilder = inject(FormBuilder);


  #mediumSubscription = new Subscription();

  constructor() {
    this.#createForm();
  }

  ngOnInit(): void {

    this.#mediumSubscription = this.medienFacade.selectedMediumDetails$.subscribe((medium) => {
      this.medium = medium;
      this.#initForm();
    });

  }

  ngOnDestroy(): void {
    this.#mediumSubscription.unsubscribe();
  }

  mediumDataError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }

  submit(): void {
    const payload: MediumDto = this.#readFormValues();
    this.medienFacade.saveMedium(payload);
  }

  showBtnDetails(): boolean {
    return this.medium.id !== 'neu';
  }

  cancelEdit(): void {
    this.medienFacade.cancelEdit(this.medium);
  }

  gotoSuche(): void {
    this.medienFacade.navigateToSuche();
  }

  formInvalid(): boolean {

    const selectedMedienart = this.form.controls['medienart'].value;

    if (selectedMedienart === '') {
      return true;
    }

    return !this.form.valid;
  }


  #createForm(): void {

    this.form = this.#formBuilder.group({
      titel: ['', [Validators.required, Validators.maxLength(100)]],
      medienart: [initialGuiMedienart.label, [Validators.required]],
      autor: ['', [Validators.maxLength(100)]],
      url: ['', [Validators.maxLength(255)]],
      kommentar: ['', [Validators.maxLength(200)]]
    });
  }

  #initForm() {

    const medienart: Medienart | undefined = this.medium.medienart;
    const theGuiMedienart: GuiMedienart = medienart === undefined ? initialGuiMedienart : new GuiMedienartenMap().getGuiMedienart(medienart);

    this.form.controls['titel'].setValue(this.medium.titel ? this.medium.titel : '');
    this.form.controls['medienart'].setValue(theGuiMedienart.label);
    this.form.controls['autor'].setValue(this.medium.autor ? this.medium.autor : '');
    this.form.controls['url'].setValue(this.medium.url ? this.medium.url : '');
    this.form.controls['kommentar'].setValue(this.medium.kommentar ? this.medium.kommentar : '');
  }

  #readFormValues(): MediumDto {

    const formValue = this.form.value;

    const medienart: Medienart = new GuiMedienartenMap().getMedienartOfLabel(formValue['medienart']);



    const payload: MediumDto = {
      id: this.medium.id,
      titel: formValue['titel'].trim(),
      medienart: medienart === 'NOOP' ? undefined : medienart,
      kommentar: formValue['kommentar'] && formValue['kommentar'].trim().length > 0 ? formValue['kommentar'].trim() : undefined,
      autor: formValue['autor'] && formValue['autor'].trim().length > 0 ? formValue['autor'].trim() : undefined,     
      url: formValue['url'] && formValue['url'].trim().length > 0 ? formValue['url'].trim() : '',
      schreibgeschuetzt: false,
      ownMedium: true
    };

    return payload;

  }
}
