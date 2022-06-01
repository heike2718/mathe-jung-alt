import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogData } from '../print-raetsel-dialog.data';


@Component({
  selector: 'mja-print-raetsel-dialog',
  templateUrl: './print-raetsel-dialog.component.html',
  styleUrls: ['./print-raetsel-dialog.component.scss'],
})
export class PrintRaetselDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<PrintRaetselDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) { }

  ngOnInit(): void { }

  onOptionSelected($event: any): void {
    const value = $event as string;
    this.data.selectedAnzeigeAntwortvorschlaege = value;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
