import { Component, OnInit } from '@angular/core';
import { Deskriptor, DeskriptorenSearchFacade } from '@mathe-jung-alt-workspace/deskriptoren/domain';

@Component({
  selector: 'mja-deskriptoren-search',
  templateUrl: './deskriptoren-search.component.html',
  styleUrls: ['./deskriptoren-search.component.scss'],
})
export class DeskriptorenSearchComponent implements OnInit {

  restliste$ = this.deskriptorenSearchFacade.restliste$;
  suchliste$ = this.deskriptorenSearchFacade.suchliste$;

  constructor(private deskriptorenSearchFacade: DeskriptorenSearchFacade) {}

  ngOnInit() {
    this.load();
  }

  private load(): void {
    this.deskriptorenSearchFacade.load();
  }

  addToSuchliste(deskriptor: Deskriptor): void {
    this.deskriptorenSearchFacade.addToSearchlist(deskriptor);    
  }

  removeFromSuchliste(deskriptor: Deskriptor): void {
    this.deskriptorenSearchFacade.removeFromSearchlist(deskriptor);
  }


}
