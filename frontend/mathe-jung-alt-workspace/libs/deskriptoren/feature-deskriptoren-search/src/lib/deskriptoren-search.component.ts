import { Component, OnInit } from '@angular/core';
import { Deskriptor, DeskriptorenFacade } from '@mathe-jung-alt-workspace/deskriptoren/domain';

@Component({
  selector: 'mja-deskriptoren-search',
  templateUrl: './deskriptoren-search.component.html',
  styleUrls: ['./deskriptoren-search.component.scss'],
})
export class DeskriptorenSearchComponent implements OnInit {

  restliste$ = this.deskriptorenFacade.restliste$;
  suchliste$ = this.deskriptorenFacade.suchliste$;

  constructor(private deskriptorenFacade: DeskriptorenFacade) {}

  ngOnInit() {
    this.load();
  }

  private load(): void {
    this.deskriptorenFacade.load();
  }

  addToSuchliste(deskriptor: Deskriptor): void {
    this.deskriptorenFacade.addToSearchlist(deskriptor);    
  }

  removeFromSuchliste(deskriptor: Deskriptor): void {
    this.deskriptorenFacade.removeFromSearchlist(deskriptor);
  }


}
