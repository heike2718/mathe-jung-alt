import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { GrafikFacade } from '@mja-workspace/grafik/domain';
import { initialUploadComponentModel, UploadComponentModel } from '@mja-workspace/shared/ui-components';
import { Message, MessageService } from '@mja-workspace/shared/util-mja';

@Component({
  selector: 'mja-grafik',
  templateUrl: './grafik-details.component.html',
  styleUrls: ['./grafik-details.component.scss'],
})
export class GrafikDetailsComponent implements OnInit {

  @Input()
  pfad!: string;
  
  uploadModel!: UploadComponentModel;

  constructor(public grafikFacade: GrafikFacade) { }

  ngOnInit(): void {
    this.uploadModel = { ...initialUploadComponentModel, pfad: this.pfad };
  }
}
