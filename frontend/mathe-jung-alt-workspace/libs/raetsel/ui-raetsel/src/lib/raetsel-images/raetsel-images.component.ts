import { Component, Input, OnInit } from '@angular/core';
import { RaetselDetails } from '@mathe-jung-alt-workspace/raetsel/domain';

@Component({
  selector: 'mja-raetsel-images',
  templateUrl: './raetsel-images.component.html',
  styleUrls: ['./raetsel-images.component.scss']
})
export class RaetselImagesComponent implements OnInit {

  @Input()
  raetsel!: RaetselDetails;

  constructor() { }

  ngOnInit(): void { }

}
