import { Component, OnInit } from '@angular/core';
import { DeskriptorenSearchFacade } from '@mathe-jung-alt-workspace/deskriptoren/domain';

@Component({
  selector: 'mja-deskriptoren-search',
  templateUrl: './deskriptoren-search.component.html',
  styleUrls: ['./deskriptoren-search.component.scss'],
})
export class DeskriptorenSearchComponent implements OnInit {
  deskriptorList$ = this.deskriptorenSearchFacade.deskriptorList$;

  constructor(private deskriptorenSearchFacade: DeskriptorenSearchFacade) {}

  ngOnInit() {
    this.load();
  }

  load(): void {
    this.deskriptorenSearchFacade.load();
  }
}
