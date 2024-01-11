import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { AufgabensammlungselementDialogData } from './aufgabensammlungselement-dialog.data';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'mja-Aufgabensammlungselement',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    FormsModule
  ],
  templateUrl: './aufgabensammlungenselement-dialog.component.html',
  styleUrls: ['./aufgabensammlungenselement-dialog.component.scss'],
})
export class AufgabensammlungselementDialogComponent {


  constructor(public dialogRef: MatDialogRef<AufgabensammlungselementDialogData>,
    @Inject(MAT_DIALOG_DATA) public data: AufgabensammlungselementDialogData) {
      this.dialogRef.disableClose = true;
  }

  save(): void {
    this.dialogRef.close(this.data);
  }

  close(): void {
    this.dialogRef.close();
  }

  submitDisabled(): boolean {

    if (!this.#isSchluesselValid()) {
      return true;
    }
    if (!this.#isNummerValid()) {
      return true
    }
    if (!this.#isPunkteValid()) {
      return true
    }

    return false;
  }

  #isSchluesselValid(): boolean {
    if (!this.data.schluessel) {
      return false;
    }

    return this.data.schluessel.trim().length === 5;
  }

  #isNummerValid(): boolean {
    if (!this.data.nummer) {
      return false;
    }
    return this.data.nummer.trim().length > 0 && this.data.nummer.trim().length <= 100;
  }

  #isPunkteValid(): boolean {

    if (!this.data.punkte) {
      return false;
    }

    if (this.data.punkte === 0) {
      return false;
    }

    const punkteAsString = '' + this.data.punkte;
    return punkteAsString.endsWith('00');
  }

}
