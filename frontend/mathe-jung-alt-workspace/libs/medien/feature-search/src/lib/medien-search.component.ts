import { Component, OnInit } from '@angular/core';
import { MedienFacade } from '@mathe-jung-alt-workspace/medien/domain';

@Component({
  selector: 'mja-medien-search',
  templateUrl: './medien-search.component.html',
  styleUrls: ['./medien-search.component.scss'],
})
export class MedienSearchComponent implements OnInit {
  medienList$ = this.medienFacade.medienList$;

  constructor(private medienFacade: MedienFacade) {}

  ngOnInit() {
    this.load();
  }

  load(): void {
    this.medienFacade.load();
  }
}
