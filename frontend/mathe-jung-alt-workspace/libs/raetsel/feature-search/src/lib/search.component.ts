import { Component, OnInit } from '@angular/core';
import { SearchFacade } from '@mathe-jung-alt-workspace/raetsel/domain';

@Component({
  selector: 'raetsel-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit {
  raetselList$ = this.searchFacade.raetselList$;

  constructor(private searchFacade: SearchFacade) {}

  ngOnInit() {
    this.load();
  }

  load(): void {
    this.searchFacade.load();
  }
}
