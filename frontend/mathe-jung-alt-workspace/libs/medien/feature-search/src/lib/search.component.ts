import { Component, OnInit } from '@angular/core';
import { SearchFacade } from '@mathe-jung-alt-workspace/medien/domain';

@Component({
  selector: 'medien-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit {
  medienList$ = this.searchFacade.medienList$;

  constructor(private searchFacade: SearchFacade) {}

  ngOnInit() {
    this.load();
  }

  load(): void {
    this.searchFacade.load();
  }
}
