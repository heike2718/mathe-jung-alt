import { Component, OnDestroy, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { RaetselgruppeBasisdaten, RaetselgruppenFacade } from '@mja-workspace/raetselgruppen/domain';
import { getSchwierigkeitsgrade, GuiSchwierigkeitsgrad, STATUS, initialSchwierigkeitsgrad, schwierigkeitsgradValueOfLabel, GuiRaetselgruppeReferenztyp, getGuiRaetselgruppeReferenztypen, initialGuiRaetselgruppeReferenztyp, raetselgruppeReferenztypOfLabel } from '@mja-workspace/shared/util-mja';
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
  selectSchwierigkeitsgradeInput: GuiSchwierigkeitsgrad[] = getSchwierigkeitsgrade();
  selectReferenztypenInput: GuiRaetselgruppeReferenztyp[] = getGuiRaetselgruppeReferenztypen();

  form!: UntypedFormGroup;

  constructor(
    public raetselgruppenFacade: RaetselgruppenFacade,
    private fb: UntypedFormBuilder) {

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      status: ['ERFASST', [Validators.required]],
      kommentar: [''],
      schwierigkeitsgrad: [initialSchwierigkeitsgrad, [Validators.required]],
      referenztyp: [initialGuiRaetselgruppeReferenztyp],
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
    console.log('jetzt speichern');
  }

  cancelEdit(): void {
    console.log('wenn neu, dann zur Suche, sonst zu den Details');
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

  #initForm() {

    this.form.controls['name'].setValue(this.#raetselgruppeBasisdaten.name ? this.#raetselgruppeBasisdaten.name : '');
    this.form.controls['status'].setValue(this.#raetselgruppeBasisdaten.status);
    this.form.controls['kommentar'].setValue(this.#raetselgruppeBasisdaten.kommentar ? this.#raetselgruppeBasisdaten.kommentar : '');
    this.form.controls['schwierigkeitsgrad'].setValue(this.#raetselgruppeBasisdaten.schwierigkeitsgrad ? schwierigkeitsgradValueOfLabel(this.#raetselgruppeBasisdaten.schwierigkeitsgrad) : initialSchwierigkeitsgrad);
    this.form.controls['referenztyp'].setValue(this.#raetselgruppeBasisdaten.referenztyp ? raetselgruppeReferenztypOfLabel(this.#raetselgruppeBasisdaten.referenztyp) : initialGuiRaetselgruppeReferenztyp);
    this.form.controls['referenz'].setValue(this.#raetselgruppeBasisdaten.referenz ? this.#raetselgruppeBasisdaten.referenz : '');
  }

}
