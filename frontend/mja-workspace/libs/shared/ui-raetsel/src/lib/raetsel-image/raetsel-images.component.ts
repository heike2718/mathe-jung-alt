import { Component, Input, OnInit } from '@angular/core';
import { GeneratedImages, RaetselDetails } from '@mja-workspace/raetsel/domain';

@Component({
  selector: 'mja-raetsel-images',
  templateUrl: './raetsel-images.component.html',
  styleUrls: ['./raetsel-images.component.scss'],
})
export class RaetselImagesComponent {

  @Input()
  images!: GeneratedImages;
}
