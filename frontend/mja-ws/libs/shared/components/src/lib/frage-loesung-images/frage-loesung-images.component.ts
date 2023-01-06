import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneratedImages } from '@mja-ws/core/model';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { MatExpansionModule } from '@angular/material/expansion';


@Component({
  selector: 'mja-frage-loesung-images',
  standalone: true,
  imports: [
    CommonModule,
    CdkAccordionModule,
    MatExpansionModule
  ],
  templateUrl: './frage-loesung-images.component.html',
  styleUrls: ['./frage-loesung-images.component.scss'],
})
export class FrageLoesungImagesComponent {

  @Input()
  images!: GeneratedImages;
}
