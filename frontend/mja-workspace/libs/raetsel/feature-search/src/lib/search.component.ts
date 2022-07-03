import { Component, OnDestroy, OnInit } from '@angular/core';
import { SearchFacade } from '@mja-workspace/raetsel/domain';
import { Suchkontext } from '@mja-workspace/suchfilter/domain';
import { Subscription } from 'rxjs';


@Component({
  selector: 'raetsel-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit, OnDestroy {
  #kontext: Suchkontext = 'RAETSEL';

  #sucheClearedSubscription: Subscription = new Subscription();
  #paginationStateSubscription: Subscription = new Subscription();
  #userAdminSubscription: Subscription = new Subscription();
  #deskriptorenLoadedSubscription: Subscription = new Subscription();
  #canStartSucheSubscription: Subscription = new Subscription();
  #suchfilterSubscription: Subscription = new Subscription();

  isAdmin = false;

  constructor(public searchFacade: SearchFacade) {

    console.log('SearchComponent created');
  }

  ngOnInit() {
  }

  ngOnDestroy(): void {
    this.#suchfilterSubscription.unsubscribe();
    this.#sucheClearedSubscription.unsubscribe();
    this.#paginationStateSubscription.unsubscribe();
    this.#userAdminSubscription.unsubscribe();
    this.#deskriptorenLoadedSubscription.unsubscribe();
    this.#canStartSucheSubscription.unsubscribe();
  }
}
