import { AfterViewInit, Component, ElementRef, EventEmitter, inject, Input, OnDestroy, Output, ViewChild } from '@angular/core';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { SelectItemsComponentModel } from '@rbk-ws/core/model';
import { debounceTime, distinctUntilChanged, fromEvent, Subscription, tap } from 'rxjs';
import { RaetselFacade } from '@rbk-ws/raetsel/api';
import { SelectItemsComponent } from '@rbk-ws/shared/components';

@Component({
    selector: 'rbk-raetsel-suchfilter-admin',
    imports: [
    MatFormFieldModule,
    MatInputModule,
    SelectItemsComponent
],
    templateUrl: './raetsel-suchfilter-admin.component.html',
    styleUrls: ['./raetsel-suchfilter-admin.component.scss']
})
export class RaetselSuchfilterAdminComponent implements AfterViewInit, OnDestroy {

  #raetselFacade = inject(RaetselFacade);

  #keySubscription: Subscription = new Subscription();
  #inputSubscription: Subscription = new Subscription();
  #suchfilterSubscription: Subscription = new Subscription();

  @ViewChild('input') input!: ElementRef;

  @Input()
  SelectItemsComponentModel!: SelectItemsComponentModel;

  @Output()
  private inputChanged: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  private selectableItemsChanged: EventEmitter<SelectItemsComponentModel> = new EventEmitter<SelectItemsComponentModel>();

  ngAfterViewInit(): void {

    this.#inputSubscription = this.#raetselFacade.suchfilter$.subscribe(

      (selectedSuchfilter) => {
        if (selectedSuchfilter) {
          this.input.nativeElement.value = selectedSuchfilter.suchstring;
        }
      }
    );

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
    this.#inputSubscription.unsubscribe();
    this.#suchfilterSubscription.unsubscribe();
  }

  public onSelectItemsComponentModelChanged($event: SelectItemsComponentModel) {
    this.selectableItemsChanged.emit($event);
  }

  #emitInputValue() {
    this.inputChanged.emit(this.input.nativeElement.value);
  }
}
