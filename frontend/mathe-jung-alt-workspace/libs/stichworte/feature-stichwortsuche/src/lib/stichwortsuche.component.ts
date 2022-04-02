import { Component, OnInit } from '@angular/core';
import { StichwortsucheFacade } from '@mathe-jung-alt-workspace/stichworte/domain';

@Component({
  selector: 'stichworte-stichwortsuche',
  templateUrl: './stichwortsuche.component.html',
  styleUrls: ['./stichwortsuche.component.scss'],
})
export class StichwortsucheComponent implements OnInit {
  stichwortList$ = this.stichwortsucheFacade.stichwortList$;

  constructor(private stichwortsucheFacade: StichwortsucheFacade) {}

  ngOnInit() {
    this.load();
  }

  load(): void {
    this.stichwortsucheFacade.load();
  }
}
