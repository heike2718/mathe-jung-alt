import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { JaNeinDialogData } from './ja-nein-dialog.model';

@Component({
  selector: 'mja-ja-nein',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule
  ],
  templateUrl: './ja-nein-dialog.component.html',
  styleUrls: ['./ja-nein-dialog.component.scss'],
})
export class JaNeinDialogComponent {

  constructor(public dialogRef: MatDialogRef<JaNeinDialogData>,
    @Inject(MAT_DIALOG_DATA) public data: JaNeinDialogData) { }
}
