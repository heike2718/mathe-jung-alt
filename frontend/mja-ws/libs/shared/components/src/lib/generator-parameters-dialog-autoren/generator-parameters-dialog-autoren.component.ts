import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { SelectGeneratorParametersUIModelAutoren } from '@mja-ws/core/model';

@Component({
  selector: 'mja-generator-parameters-dialog-autoren',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './generator-parameters-dialog-autoren.component.html',
  styleUrls: ['./generator-parameters-dialog-autoren.component.scss'],
})
export class GeneratorParametersDialogAutorenComponent {

  // selectedOption: string | undefined;

  public dialogRef: MatDialogRef<GeneratorParametersDialogAutorenComponent> = inject(MatDialogRef);

  constructor(@Inject(MAT_DIALOG_DATA) public data: SelectGeneratorParametersUIModelAutoren) {}
}
