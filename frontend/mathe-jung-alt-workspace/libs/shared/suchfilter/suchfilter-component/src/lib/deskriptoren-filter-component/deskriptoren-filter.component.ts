import { AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Deskriptor, DeskriptorenSearchFacade } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
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

  restliste$ = this.deskriptorenSearchFacade.restliste$;
  suchliste$ = this.deskriptorenSearchFacade.suchliste$;

  private suchlisteSubscription: Subscription = new Subscription();

  constructor(private deskriptorenSearchFacade: DeskriptorenSearchFacade) { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {

    this.deskriptorenSearchFacade.load(this.kontext);

    this.suchlisteSubscription = this.deskriptorenSearchFacade.suchliste$.pipe(
      tap((liste: Deskriptor[]) => this.suchlisteDeskriptorenChanged.emit(liste))
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.suchlisteSubscription.unsubscribe();
  }

  addToSuchliste(deskriptor: Deskriptor): void {
    this.deskriptorenSearchFacade.addToSearchlist(deskriptor);
  }

  removeFromSuchliste(deskriptor: Deskriptor): void {
    this.deskriptorenSearchFacade.removeFromSearchlist(deskriptor);
  }
}
