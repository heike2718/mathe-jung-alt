import { Component, OnInit } from '@angular/core';
import { BilderFacade } from '@mathe-jung-alt-workspace/bilder/domain';

@Component({
  selector: 'bilder-search',
  templateUrl: './bilder-search.component.html',
  styleUrls: ['./bilder-search.component.scss'],
})
export class BilderSearchComponent implements OnInit {
  bildList$ = this.searchFacade.bildList$;

  constructor(private searchFacade: BilderFacade) {}

  ngOnInit() {
    this.load();
  }

  load(): void {
    this.searchFacade.load();
  }
}
