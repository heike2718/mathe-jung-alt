import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { SelectGeneratorParametersUIModel } from '@mja-ws/core/model';

@Component({
  selector: 'mja-generator-parameters-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './generator-parameters-dialog.component.html',
  styleUrls: ['./generator-parameters-dialog.component.scss'],
})
export class GeneratorParametersDialogComponent {

  public dialogRef: MatDialogRef<GeneratorParametersDialogComponent> = inject(MatDialogRef);

  constructor(@Inject(MAT_DIALOG_DATA) public data: SelectGeneratorParametersUIModel) {
    this.dialogRef.disableClose = true;
  }
}
