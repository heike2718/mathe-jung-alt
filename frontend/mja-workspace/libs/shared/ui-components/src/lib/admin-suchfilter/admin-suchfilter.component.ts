import { Component, ElementRef, Input, Output, ViewChild, EventEmitter, OnDestroy, AfterViewInit } from '@angular/core';
import { Deskriptor, Suchkontext } from '@mja-workspace/suchfilter/domain';
import { debounceTime, distinctUntilChanged, fromEvent, Subscription, tap } from "rxjs";

@Component({
  selector: 'mja-admin-suchfilter',
  templateUrl: './admin-suchfilter.component.html',
  styleUrls: ['./admin-suchfilter.component.scss'],
})
export class AdminSuchfilterComponent implements AfterViewInit, OnDestroy {

  @Input()
  public kontext!: Suchkontext;

  @ViewChild('input') input!: ElementRef;

  @Output()
  private inputChanged: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  private suchlisteDeskriptorenChanged: EventEmitter<Deskriptor[]> = new EventEmitter<Deskriptor[]>();

  #keySubscription: Subscription = new Subscription();

  constructor() { }

  ngAfterViewInit(): void {
    this.#keySubscription = fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(() => {
          this.#emitInputValue();
        })
      )
      .subscribe();
  }

  ngOnDestroy(): void {
    this.#keySubscription.unsubscribe();
  }

  public onDeskriptorenChanged($event: Deskriptor[]) {
    this.suchlisteDeskriptorenChanged.emit($event);
  }


  #emitInputValue() {
    this.inputChanged.emit(this.input.nativeElement.value);
  }
}
