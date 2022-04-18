import { AfterViewInit, Component, ElementRef, EventEmitter, OnDestroy, Output, ViewChild } from "@angular/core";
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

  private keySubscription: Subscription = new Subscription();

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
  }

  private emitInputValue() {    
    this.inputChanged.emit(this.input.nativeElement.value);
  }
}