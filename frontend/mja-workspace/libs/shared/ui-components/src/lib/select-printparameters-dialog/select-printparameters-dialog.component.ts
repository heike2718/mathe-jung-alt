import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SelectPrintparametersDialogData } from './select-printparameters-dialog.data';

@Component({
  selector: 'mja-select-printparameters-dialog',
  templateUrl: './select-printparameters-dialog.component.html',
  styleUrls: ['./select-printparameters-dialog.component.scss'],
})
export class SelectPrintparametersDialogComponent implements OnInit {
  
  selectedOption: string | undefined;

  constructor(public dialogRef: MatDialogRef<SelectPrintparametersDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SelectPrintparametersDialogData) { }

  ngOnInit(): void { }

  onOptionSelected($event: any): void {
    const value = $event as string;
    this.data.selectedLayoutAntwortvorschlaege = value;
  }
}
