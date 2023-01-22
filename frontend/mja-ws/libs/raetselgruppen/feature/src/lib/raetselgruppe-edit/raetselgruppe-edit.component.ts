import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselgruppenFacade } from '@mja-ws/raetselgruppen/api';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TextFieldModule } from '@angular/cdk/text-field';
import { STATUS, GuiSchwierigkeitsgradeMap, GuiReferenztypenMap, initialGuiSchwierigkeitsgrad, initialGuiReferenztyp, GuiSchwierigkeitsgrad, GuiRefereztyp, Referenztyp, Schwierigkeitsgrad } from '@mja-ws/core/model';
import { Router } from '@angular/router';
import { EditRaetselgruppePayload, RaetselgruppeBasisdaten } from '@mja-ws/raetselgruppen/model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-raetselgruppe-edit',
  standalone: true,
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatButtonModule,
    MatInputModule,
    MatInputModule,
    MatFormFieldModule,
    MatListModule,
    FormsModule,
    TextFieldModule,
    ReactiveFormsModule
  ],
  templateUrl: './raetselgruppe-edit.component.html',
  styleUrls: ['./raetselgruppe-edit.component.scss'],
})
export class RaetselgruppeEditComponent implements OnInit, OnDestroy {

  raetselgruppenFacade = inject(RaetselgruppenFacade);

  selectStatusInput: STATUS[] = ['ERFASST', 'FREIGEGEBEN'];
  selectSchwierigkeitsgradeInput: string[] = new GuiSchwierigkeitsgradeMap().getLabelsSorted();
  selectReferenztypenSelectContent: string[] = new GuiReferenztypenMap().getLabelsSorted();

  form!: FormGroup;

  #formBuilder = inject(FormBuilder);
  #router = inject(Router);

  #raetselgruppeBasisdaten!: RaetselgruppeBasisdaten;

  #raetselgruppeBasisdatenSubscription = new Subscription();

  constructor() {
    this.#createForm();
  }




  ngOnInit(): void {

    this.#raetselgruppeBasisdatenSubscription = this.raetselgruppenFacade.raetselgruppeDetails$.subscribe((basisdaten) => {

      this.#raetselgruppeBasisdaten = basisdaten;
      this.#initForm();

    });
  }

  ngOnDestroy(): void {
    this.#raetselgruppeBasisdatenSubscription.unsubscribe();
  }

  raetselgruppeDataError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }

  submit(): void {
    const editRaetselgruppePayload: EditRaetselgruppePayload = this.#readFormValues();
    this.raetselgruppenFacade.saveRaetselgruppe(editRaetselgruppePayload);
  }

  showBtnDetails(): boolean {
    return this.#raetselgruppeBasisdaten.id !== 'neu';
  }

  cancelEdit(): void {
    this.raetselgruppenFacade.cancelEdit(this.#raetselgruppeBasisdaten);
  }

  gotoUebersicht(): void {
    this.raetselgruppenFacade.unselectRaetselgruppe();
    this.#router.navigateByUrl('raetselgruppen/uebersicht');      
  }

  formInvalid(): boolean {

    const selectedLevel = this.form.controls['schwierigkeitsgrad'].value;
    const selectedRefTyp = this.form.controls['referenztyp'].value;
    const referenz = this.form.controls['referenz'].value;

    if (selectedLevel === '') {
      return true;
    }

    if (selectedRefTyp !== '' && (!referenz || referenz.trim().length === 0)) {
      return true;
    }

    return !this.form.valid;
  }

  #createForm(): void {
    this.form = this.#formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      status: ['ERFASST', [Validators.required]],
      kommentar: [''],
      schwierigkeitsgrad: [initialGuiSchwierigkeitsgrad.label, [Validators.required]],
      referenztyp: [initialGuiReferenztyp.label],
      referenz: ['']
    });
  }

  #initForm() {

    this.form.controls['name'].setValue(this.#raetselgruppeBasisdaten.name ? this.#raetselgruppeBasisdaten.name : '');
    this.form.controls['status'].setValue(this.#raetselgruppeBasisdaten.status);
    this.form.controls['kommentar'].setValue(this.#raetselgruppeBasisdaten.kommentar ? this.#raetselgruppeBasisdaten.kommentar : '');


    const guiSchwierigkeitsrad: GuiSchwierigkeitsgrad = this.#raetselgruppeBasisdaten && this.#raetselgruppeBasisdaten.schwierigkeitsgrad ? new GuiSchwierigkeitsgradeMap().getGuiSchwierigkeitsgrade(this.#raetselgruppeBasisdaten.schwierigkeitsgrad)
      : initialGuiSchwierigkeitsgrad;
    this.form.controls['schwierigkeitsgrad'].setValue(guiSchwierigkeitsrad.label);

    let guiReferenztyp: GuiRefereztyp = initialGuiReferenztyp;

    if (this.#raetselgruppeBasisdaten && this.#raetselgruppeBasisdaten.referenztyp) {
      guiReferenztyp = new GuiReferenztypenMap().getGuiRefereztyp(this.#raetselgruppeBasisdaten.referenztyp);
    }

    this.form.controls['referenztyp'].setValue(guiReferenztyp.label);

    this.form.controls['referenz'].setValue(this.#raetselgruppeBasisdaten.referenz ? this.#raetselgruppeBasisdaten.referenz : '');
  }

  #readFormValues(): EditRaetselgruppePayload {

    const formValue = this.form.value;

    const referenztyp: Referenztyp = new GuiReferenztypenMap().getReferenztypOfLabel(formValue['referenztyp']);
    const schwierigkeitsgrad: Schwierigkeitsgrad = new GuiSchwierigkeitsgradeMap().getSchwierigkeitsgradOfLabel(formValue['schwierigkeitsgrad']);

    const editRaetselgruppePayload: EditRaetselgruppePayload = {
      id: this.#raetselgruppeBasisdaten.id,
      name: formValue['name'].trim(),
      referenz: formValue['referenz'] && formValue['referenz'].trim().length > 0 ? formValue['referenz'].trim() : undefined,
      referenztyp: referenztyp === 'NOOP' ? undefined : referenztyp,
      schwierigkeitsgrad: schwierigkeitsgrad,
      status: formValue['status'],
      kommentar: formValue['kommentar'] && formValue['kommentar'].trim().length > 0 ? formValue['kommentar'].trim() : undefined
    };

    return editRaetselgruppePayload;

  }
}
