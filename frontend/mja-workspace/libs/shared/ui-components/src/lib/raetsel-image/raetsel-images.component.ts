import { Component, Input } from '@angular/core';
import { GeneratedImages } from './raetsel-images.model';

@Component({
  selector: 'mja-raetsel-images',
  templateUrl: './raetsel-images.component.html',
  styleUrls: ['./raetsel-images.component.scss'],
})
export class RaetselImagesComponent {

  @Input()
  images!: GeneratedImages;
}
