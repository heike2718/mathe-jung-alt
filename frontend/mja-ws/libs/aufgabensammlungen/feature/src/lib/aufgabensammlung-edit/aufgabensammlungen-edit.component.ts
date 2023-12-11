import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AufgabensammlungenFacade } from '@mja-ws/aufgabensammlungen/api';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatListModule } from '@angular/material/list';
import { TextFieldModule } from '@angular/cdk/text-field';
import { GuiSchwierigkeitsgradeMap, GuiReferenztypenMap, initialGuiSchwierigkeitsgrad, initialGuiReferenztyp, GuiSchwierigkeitsgrad, GuiRefereztyp, Referenztyp, Schwierigkeitsgrad } from '@mja-ws/core/model';
import { Router } from '@angular/router';
import { EditAufgabensammlungPayload, AufgabensammlungBasisdaten } from '@mja-ws/aufgabensammlungen/model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-aufgabensammlung-edit',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatGridListModule,
    MatInputModule,
    MatInputModule,
    MatFormFieldModule,
    MatListModule,
    FormsModule,
    TextFieldModule,
    ReactiveFormsModule
  ],
  templateUrl: './aufgabensammlungen-edit.component.html',
  styleUrls: ['./aufgabensammlungen-edit.component.scss'],
})
export class AufgabensammlungEditComponent implements OnInit, OnDestroy {

  aufgabensammlungenFacade = inject(AufgabensammlungenFacade);

  selectStatusInput: string[] = ['ERFASST', 'FREIGEGEBEN'];
  selectSchwierigkeitsgradeInput: string[] = new GuiSchwierigkeitsgradeMap().getLabelsSorted();
  selectReferenztypenSelectContent: string[] = new GuiReferenztypenMap().getLabelsSorted();

  form!: FormGroup;

  #formBuilder = inject(FormBuilder);
  #router = inject(Router);

  #aufgabensammlungBasisdaten!: AufgabensammlungBasisdaten;

  #aufgabensammlungBasisdatenSubscription = new Subscription();

  constructor() {
    this.#createForm();
  }

  ngOnInit(): void {

    this.#aufgabensammlungBasisdatenSubscription = this.aufgabensammlungenFacade.aufgabensammlungBasisdaten$.subscribe((basisdaten) => {

      this.#aufgabensammlungBasisdaten = basisdaten;
      this.#initForm();

    });
  }

  ngOnDestroy(): void {
    this.#aufgabensammlungBasisdatenSubscription.unsubscribe();
  }

  raetselgruppeDataError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }

  submit(): void {
    const EditAufgabensammlungPayload: EditAufgabensammlungPayload = this.#readFormValues();
    this.aufgabensammlungenFacade.saveAufgabensammlung(EditAufgabensammlungPayload);
  }

  showBtnDetails(): boolean {
    return this.#aufgabensammlungBasisdaten.id !== 'neu';
  }

  cancelEdit(): void {
    this.aufgabensammlungenFacade.cancelEdit(this.#aufgabensammlungBasisdaten);
  }

  gotoUebersicht(): void {
    this.aufgabensammlungenFacade.unselectAufgabensammlung();
    this.#router.navigateByUrl('aufgabensammlungen/uebersicht');      
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

    // TODO: checkbox privat muss noch in die form

    this.form = this.#formBuilder.group({
      id: [''],
      user: [''],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      status: ['ERFASST', [Validators.required]],
      kommentar: [''],
      schwierigkeitsgrad: [initialGuiSchwierigkeitsgrad.label, [Validators.required]],
      referenztyp: [initialGuiReferenztyp.label],
      referenz: ['']
    });
  }

  #initForm() {

    const theStatus: string = this.#aufgabensammlungBasisdaten.freigegeben ? 'FREIGEGEBEN' : 'ERFASST';

    this.form.controls['id'].setValue(this.#aufgabensammlungBasisdaten.id);
    this.form.controls['user'].setValue(this.#aufgabensammlungBasisdaten.geaendertDurch ? this.#aufgabensammlungBasisdaten.geaendertDurch : ' ');
    this.form.controls['name'].setValue(this.#aufgabensammlungBasisdaten.name ? this.#aufgabensammlungBasisdaten.name : '');
    this.form.controls['status'].setValue(theStatus);
    this.form.controls['kommentar'].setValue(this.#aufgabensammlungBasisdaten.kommentar ? this.#aufgabensammlungBasisdaten.kommentar : '');


    const guiSchwierigkeitsrad: GuiSchwierigkeitsgrad = this.#aufgabensammlungBasisdaten && this.#aufgabensammlungBasisdaten.schwierigkeitsgrad ? new GuiSchwierigkeitsgradeMap().getGuiSchwierigkeitsgrade(this.#aufgabensammlungBasisdaten.schwierigkeitsgrad)
      : initialGuiSchwierigkeitsgrad;
    this.form.controls['schwierigkeitsgrad'].setValue(guiSchwierigkeitsrad.label);

    let guiReferenztyp: GuiRefereztyp = initialGuiReferenztyp;

    if (this.#aufgabensammlungBasisdaten && this.#aufgabensammlungBasisdaten.referenztyp) {
      guiReferenztyp = new GuiReferenztypenMap().getGuiRefereztyp(this.#aufgabensammlungBasisdaten.referenztyp);
    }

    this.form.controls['referenztyp'].setValue(guiReferenztyp.label);

    this.form.controls['referenz'].setValue(this.#aufgabensammlungBasisdaten.referenz ? this.#aufgabensammlungBasisdaten.referenz : '');
  }

  #readFormValues(): EditAufgabensammlungPayload {

    const formValue = this.form.value;

    const referenztyp: Referenztyp = new GuiReferenztypenMap().getReferenztypOfLabel(formValue['referenztyp']);
    const schwierigkeitsgrad: Schwierigkeitsgrad = new GuiSchwierigkeitsgradeMap().getSchwierigkeitsgradOfLabel(formValue['schwierigkeitsgrad']);

    const EditAufgabensammlungPayload: EditAufgabensammlungPayload = {
      id: this.#aufgabensammlungBasisdaten.id,
      name: formValue['name'].trim(),
      referenz: formValue['referenz'] && formValue['referenz'].trim().length > 0 ? formValue['referenz'].trim() : undefined,
      referenztyp: referenztyp === 'NOOP' ? undefined : referenztyp,
      schwierigkeitsgrad: schwierigkeitsgrad,
      freigegeben: formValue['status'] === 'FREIGEGEBEN',
      privat: false,
      kommentar: formValue['kommentar'] && formValue['kommentar'].trim().length > 0 ? formValue['kommentar'].trim() : undefined
    };

    return EditAufgabensammlungPayload;

  }
}
