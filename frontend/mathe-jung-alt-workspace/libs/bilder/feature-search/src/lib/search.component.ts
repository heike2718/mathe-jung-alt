import { Component, OnInit } from '@angular/core';
import { SearchFacade } from '@mathe-jung-alt-workspace/bilder/domain';

@Component({
  selector: 'bilder-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit {
  bildList$ = this.searchFacade.bildList$;

  constructor(private searchFacade: SearchFacade) {}

  ngOnInit() {
    this.load();
  }

  load(): void {
    this.searchFacade.load();
  }
}
