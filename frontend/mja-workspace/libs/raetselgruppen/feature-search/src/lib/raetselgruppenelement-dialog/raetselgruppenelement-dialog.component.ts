import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RaetselgruppenelementDialogData } from './raetselgruppenelement-dialog.data';

@Component({
  selector: 'mja-raetselgruppenelement',
  templateUrl: './raetselgruppenelement-dialog.component.html',
  styleUrls: ['./raetselgruppenelement-dialog.component.scss'],
})
export class RaetselgruppenelementDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<RaetselgruppenelementDialogData>,
    @Inject(MAT_DIALOG_DATA) public data: RaetselgruppenelementDialogData) {
  }

  ngOnInit(): void { }

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
