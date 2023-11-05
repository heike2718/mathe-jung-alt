import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { ImageDialogModel } from './image-dialog.model';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'mja-ws-image-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule
  ],
  templateUrl: './image-dialog.component.html',
  styleUrls: ['./image-dialog.component.scss'],
})
export class ImageDialogComponent {

  constructor(public dialogRef: MatDialogRef<ImageDialogModel>,
    @Inject(MAT_DIALOG_DATA) public data: ImageDialogModel) { }

}
