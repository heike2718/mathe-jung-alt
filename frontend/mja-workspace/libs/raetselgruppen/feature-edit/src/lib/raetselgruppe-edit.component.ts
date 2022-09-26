import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EditRaetselgruppePayload, RaetselgruppeBasisdaten, RaetselgruppenFacade } from '@mja-workspace/raetselgruppen/domain';
import { GuiSchwierigkeitsgrad, STATUS, initialSchwierigkeitsgrad, getReferenztypenSelectContent, guiSchwierigkeitsgradValueOfId, getSchwierigkeitsgradeSelectContent, initialGuiRaetselgruppeReferenztyp, GuiRaetselgruppeReferenztyp, guiReferenztypOfId, Referenztyp, raetselgruppeReferenztypOfLabel, Schwierigkeitsgrad, schwierigkeitsgradValueOfLabel } from '@mja-workspace/shared/util-mja';
import { Subscription, tap } from 'rxjs';

@Component({
  selector: 'mja-raetselgruppe-edit',
  templateUrl: './raetselgruppe-edit.component.html',
  styleUrls: ['./raetselgruppe-edit.component.scss'],
})
export class RaetselgruppeEditComponent implements OnInit, OnDestroy {

  #raetselgruppeBasisdatenSubscription = new Subscription();
  #raetselgruppeBasisdaten!: RaetselgruppeBasisdaten;

  selectStatusInput: STATUS[] = ['ERFASST', 'FREIGEGEBEN'];
  selectSchwierigkeitsgradeInput: string[] = getSchwierigkeitsgradeSelectContent();
  selectReferenztypenSelectContent: string[] = getReferenztypenSelectContent();

  form!: FormGroup;

  constructor(
    public raetselgruppenFacade: RaetselgruppenFacade,
    private fb: FormBuilder,
    private router: Router) {

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      status: ['ERFASST', [Validators.required]],
      kommentar: [''],
      schwierigkeitsgrad: [initialSchwierigkeitsgrad.label, [Validators.required]],
      referenztyp: [initialGuiRaetselgruppeReferenztyp.label],
      referenz: ['']
    });
  }

  ngOnInit() {

    this.#raetselgruppeBasisdatenSubscription = this.raetselgruppenFacade.editorContent$.pipe(
      tap((editorContent) => {
        if (editorContent) {
          this.#raetselgruppeBasisdaten = editorContent;
          this.#initForm();
        }
      })
    ).subscribe();

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

  cancelEdit(): void {
    if (this.#raetselgruppeBasisdaten) {

      if (this.#raetselgruppeBasisdaten.id === 'neu') {
        this.raetselgruppenFacade.unselectRaetselgruppe();
      } else {
        this.router.navigateByUrl('raetselgruppen/details');
      }
    } else {
      this.raetselgruppenFacade.unselectRaetselgruppe();
    }
  }

  formInvalid(): boolean {

    const selectedLevel = this.form.controls['schwierigkeitsgrad'].value;
    const selectedRefTyp = this.form.controls['referenztyp'].value;
    const referenz = this.form.controls['referenz'].value;

    if (selectedLevel === 'NOOP') {
      return true;
    }

    if (selectedRefTyp !== 'NOOP' && (!referenz || referenz.trim().length === 0)) {
      return true;
    }

    return !this.form.valid;
  }

  #readFormValues(): EditRaetselgruppePayload {

    const formValue = this.form.value;

    const referenztyp: Referenztyp = raetselgruppeReferenztypOfLabel(formValue['referenztyp']);
    const schwierigkeitsgrad: Schwierigkeitsgrad = schwierigkeitsgradValueOfLabel(formValue['schwierigkeitsgrad']);

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

  #initForm() {

    this.form.controls['name'].setValue(this.#raetselgruppeBasisdaten.name ? this.#raetselgruppeBasisdaten.name : '');
    this.form.controls['status'].setValue(this.#raetselgruppeBasisdaten.status);
    this.form.controls['kommentar'].setValue(this.#raetselgruppeBasisdaten.kommentar ? this.#raetselgruppeBasisdaten.kommentar : '');


    const guiSchwierigkeitsrad: GuiSchwierigkeitsgrad = this.#raetselgruppeBasisdaten ? guiSchwierigkeitsgradValueOfId(this.#raetselgruppeBasisdaten.schwierigkeitsgrad) : initialSchwierigkeitsgrad;
    this.form.controls['schwierigkeitsgrad'].setValue(guiSchwierigkeitsrad.label);

    const guiReferenztyp: GuiRaetselgruppeReferenztyp = this.#raetselgruppeBasisdaten ? guiReferenztypOfId(this.#raetselgruppeBasisdaten.referenztyp) : initialGuiRaetselgruppeReferenztyp;
    this.form.controls['referenztyp'].setValue(guiReferenztyp.label);
 
    this.form.controls['referenz'].setValue(this.#raetselgruppeBasisdaten.referenz ? this.#raetselgruppeBasisdaten.referenz : '');
  }

}
