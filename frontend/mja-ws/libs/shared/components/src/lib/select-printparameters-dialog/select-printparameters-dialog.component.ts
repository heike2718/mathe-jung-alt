import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { SelectPrintparametersDialogData } from '@mja-ws/core/model';

@Component({
  selector: 'mja-select-printparameters-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './select-printparameters-dialog.component.html',
  styleUrls: ['./select-printparameters-dialog.component.scss'],
})
export class SelectPrintparametersDialogComponent {

  selectedOption: string | undefined;

  public dialogRef: MatDialogRef<SelectPrintparametersDialogComponent> = inject(MatDialogRef);

  constructor(@Inject(MAT_DIALOG_DATA) public data: SelectPrintparametersDialogData) {}

  onOptionSelected($event: any): void {
    const value = $event as string;
    this.data.selectedLayoutAntwortvorschlaege = value;
  }
}
