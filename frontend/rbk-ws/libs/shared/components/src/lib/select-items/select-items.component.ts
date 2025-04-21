import { Component, EventEmitter, inject, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatChipsModule } from '@angular/material/chips';
import { MatListModule } from '@angular/material/list';
import { Subscription } from 'rxjs';
import { SelectItemsFacade } from '@rbk-ws/core/api';
import { SelectableItem, SelectItemsComponentModel } from '@rbk-ws/core/model';

@Component({
    selector: 'rbk-select-items',
    imports: [
        CommonModule,
        MatChipsModule,
        MatListModule
    ],
    templateUrl: './select-items.component.html',
    styleUrls: ['./select-items.component.scss']
})
export class SelectItemsComponent implements OnInit, OnDestroy {

  #modelSubscription: Subscription = new Subscription();

  selectItemsFacade = inject(SelectItemsFacade);

  @Input()
  model!: SelectItemsComponentModel;

  @Output()
  modelChanged: EventEmitter<SelectItemsComponentModel> = new EventEmitter<SelectItemsComponentModel>();

  ngOnInit(): void {

    this.selectItemsFacade.init(this.model);
    this.#modelSubscription = this.selectItemsFacade.selectableItemsModel$.subscribe(
      model => this.modelChanged.emit(model)
    );
  }

  ngOnDestroy(): void {
    this.#modelSubscription.unsubscribe();
  }

  auswaehlen(item: SelectableItem): void {
    this.selectItemsFacade.addToGewaehlt(item);
  }

  verwerfen(item: SelectableItem): void {
    this.selectItemsFacade.removeFromGewaehlt(item);
  }
}
