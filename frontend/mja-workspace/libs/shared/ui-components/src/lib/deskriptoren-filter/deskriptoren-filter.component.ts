import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Deskriptor, Suchfilter, SuchfilterFacade, Suchkontext } from '@mja-workspace/suchfilter/domain';
import { Subscription, tap } from 'rxjs';

@Component({
  selector: 'mja-deskriptoren-filter',
  templateUrl: './deskriptoren-filter.component.html',
  styleUrls: ['./deskriptoren-filter.component.scss'],
})
export class DeskriptorenFilterComponent implements OnInit, OnDestroy {

  @Input()
  public suchfilter!: Suchfilter;

  @Output()
  private suchlisteDeskriptorenChanged: EventEmitter<Deskriptor[]> = new EventEmitter<Deskriptor[]>();

  restliste$ = this.suchfilterFacade.restliste$;
  suchliste$ = this.suchfilterFacade.suchliste$;

  #suchlisteSubscription: Subscription = new Subscription();
  #deskriptorenLoadedSubscription: Subscription = new Subscription();

  constructor(private suchfilterFacade: SuchfilterFacade) { }

  ngOnInit(): void {

    this.#deskriptorenLoadedSubscription = this.suchfilterFacade.deskriptorenLoaded$.subscribe(
      (loaded) => {
        if (loaded) {
          this.suchfilterFacade.setSuchfilter(this.suchfilter);
        }
      }
    );

    this.#suchlisteSubscription = this.suchliste$.pipe(
      tap((liste: Deskriptor[]) => this.suchlisteDeskriptorenChanged.emit(liste))
    ).subscribe();
  }

  ngOnDestroy(): void {
      this.#suchlisteSubscription.unsubscribe();
      this.#deskriptorenLoadedSubscription.unsubscribe();
  }

  addToSuchliste(deskriptor: Deskriptor): void {
    this.suchfilterFacade.addToSearchlist(deskriptor);
  }

  removeFromSuchliste(deskriptor: Deskriptor): void {
    this.suchfilterFacade.removeFromSearchlist(deskriptor);
  }
}
