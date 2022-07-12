import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { JaNeinDialogData } from './ja-nein-dialog.data';

@Component({
  selector: 'mja-ja-nein',
  templateUrl: './ja-nein-dialog.component.html',
  styleUrls: ['./ja-nein-dialog.component.scss'],
})
export class JaNeinDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<JaNeinDialogData>,
    @Inject(MAT_DIALOG_DATA) public data: JaNeinDialogData) { }

  ngOnInit(): void { }
}
