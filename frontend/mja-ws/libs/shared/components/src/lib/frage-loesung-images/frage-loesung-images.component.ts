import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GeneratedImages } from '@mja-ws/core/model';

@Component({
  selector: 'mja-frage-loesung-images',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './frage-loesung-images.component.html',
  styleUrls: ['./frage-loesung-images.component.scss'],
})
export class FrageLoesungImagesComponent {

  @Input()
  images!: GeneratedImages;
}
