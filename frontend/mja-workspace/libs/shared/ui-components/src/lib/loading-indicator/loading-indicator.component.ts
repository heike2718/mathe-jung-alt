import { Component, OnInit } from '@angular/core';
import { LoadingIndicatorService } from '@mja-workspace/shared/util-mja';

@Component({
  selector: 'mja-loading-indicator',
  templateUrl: './loading-indicator.component.html',
  styleUrls: ['./loading-indicator.component.scss'],
})
export class LoadingIndicatorComponent implements OnInit {

  constructor(public loadingService: LoadingIndicatorService) { }

  ngOnInit(): void { }
}
