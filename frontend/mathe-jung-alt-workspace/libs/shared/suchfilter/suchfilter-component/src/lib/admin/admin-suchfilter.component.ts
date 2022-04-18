import { AfterViewInit, Component, ElementRef, EventEmitter, OnDestroy, Output, ViewChild } from "@angular/core";
import { Deskriptor, DeskriptorenSearchFacade } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { debounceTime, distinctUntilChanged, fromEvent, Subscription, tap } from "rxjs";


@Component({
  selector: 'mja-admin-suchfilter',
  templateUrl: './admin-suchfilter.component.html',
  styleUrls: ['./admin-suchfilter.component.scss'],
})
export class AdminSuchfilterComponent implements AfterViewInit, OnDestroy {


  @ViewChild('input') input!: ElementRef;

  @Output()
  private inputChanged: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  private suchlisteDeskriptorenChanged: EventEmitter<Deskriptor[]> = new EventEmitter<Deskriptor[]>();

  private keySubscription: Subscription = new Subscription();
  private deskriptorSuchlisteSubscription: Subscription = new Subscription();

  ngAfterViewInit(): void {

    this.keySubscription = fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(() => {
          this.emitInputValue();
        })
      )
      .subscribe();
  }

  ngOnDestroy(): void {
    this.keySubscription.unsubscribe();
    this.deskriptorSuchlisteSubscription.unsubscribe();
  }

  private emitInputValue() {
    this.inputChanged.emit(this.input.nativeElement.value);
  }

  public onDeskriptorenChanged($event: Deskriptor[]) {
    this.suchlisteDeskriptorenChanged.emit($event);
  }
}