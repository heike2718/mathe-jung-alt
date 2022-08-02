import { Component, Input, OnInit } from '@angular/core';
import { GrafikFacade } from '@mja-workspace/grafik/domain';
import { Message } from '@mja-workspace/shared/util-mja';

@Component({
  selector: 'mja-grafik',
  templateUrl: './grafik-details.component.html',
  styleUrls: ['./grafik-details.component.scss'],
})
export class GrafikDetailsComponent implements OnInit {
  
  constructor(public grafikFacade: GrafikFacade) {}

  
  ngOnInit(): void {}
}
