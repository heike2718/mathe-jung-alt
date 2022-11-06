import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PrintRaetselDialogData } from './print-raetsel-dialog.data';

@Component({
  selector: 'mja-print-raetsel-dialog',
  templateUrl: './print-raetsel-dialog.component.html',
  styleUrls: ['./print-raetsel-dialog.component.scss'],
})
export class PrintRaetselDialogComponent implements OnInit {
  
  selectedOption: string | undefined;

  constructor(public dialogRef: MatDialogRef<PrintRaetselDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: PrintRaetselDialogData) { }

  ngOnInit(): void { }

  onOptionSelected($event: any): void {
    const value = $event as string;
    this.data.selectedLayoutAntwortvorschlaege = value;
  }
}
