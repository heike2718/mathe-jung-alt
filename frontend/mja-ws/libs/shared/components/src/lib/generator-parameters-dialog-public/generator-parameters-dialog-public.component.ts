import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'mja-generator-parameters-dialog-public',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './generator-parameters-dialog-public.component.html',
  styleUrls: ['./generator-parameters-dialog-public.component.scss'],
})
export class GeneratorParametersDialogPublicComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
