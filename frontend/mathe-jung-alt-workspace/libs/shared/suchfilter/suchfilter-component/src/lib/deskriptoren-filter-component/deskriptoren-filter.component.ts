import { AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { SuchfilterFacade, Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { Subscription, tap } from 'rxjs';

@Component({
  selector: 'mja-deskriptoren-filter',
  templateUrl: './deskriptoren-filter.component.html',
  styleUrls: ['./deskriptoren-filter.component.scss'],
})
export class DeskriptorenFilterComponent implements OnInit, AfterViewInit, OnDestroy {

  @Output()
  private suchlisteDeskriptorenChanged: EventEmitter<Deskriptor[]> = new EventEmitter<Deskriptor[]>();

  @Input()
  public kontext!: Suchkontext;

  restliste$ = this.suchfilterFacade.restliste$;
  suchliste$ = this.suchfilterFacade.suchliste$;

  #suchlisteSubscription: Subscription = new Subscription();

  constructor(private suchfilterFacade: SuchfilterFacade) { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {

    this.suchfilterFacade.loadDeskriptoren();

    this.#suchlisteSubscription = this.suchliste$.pipe(
      tap((liste: Deskriptor[]) => this.suchlisteDeskriptorenChanged.emit(liste))
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.#suchlisteSubscription.unsubscribe();
  }

  addToSuchliste(deskriptor: Deskriptor): void {
    this.suchfilterFacade.addToSearchlist(deskriptor);
  }

  removeFromSuchliste(deskriptor: Deskriptor): void {
    this.suchfilterFacade.removeFromSearchlist(deskriptor);
  }
}
