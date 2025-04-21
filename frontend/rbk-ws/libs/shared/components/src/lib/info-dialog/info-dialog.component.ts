import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { InfoDialogModel } from './info-dialog.model';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
@Component({
    selector: 'rbk-info-dialog',
    imports: [
        CommonModule,
        MatButtonModule,
        MatDialogModule,
        MatInputModule
    ],
    templateUrl: './info-dialog.component.html',
    styleUrls: ['./info-dialog.component.scss']
})
export class InfoDialogComponent {


  constructor(public dialogRef: MatDialogRef<InfoDialogModel>,
    @Inject(MAT_DIALOG_DATA) public data: InfoDialogModel) { }
}
