import { Component, OnInit } from '@angular/core';
import { SearchFacade } from '@mathe-jung-alt-workspace/quellen/domain';

@Component({
  selector: 'quellen-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit {
  quelleList$ = this.searchFacade.quelleList$;

  constructor(private searchFacade: SearchFacade) {}

  ngOnInit() {
    this.load();
  }

  load(): void {
    this.searchFacade.load();
  }
}