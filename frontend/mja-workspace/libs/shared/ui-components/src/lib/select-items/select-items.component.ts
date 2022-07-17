import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { SelectableItem } from '@mja-workspace/shared/util-mja';
import { Subscription } from 'rxjs';
import { SelectItemsFacade } from './select-items.facade';
import { SelectItemsCompomentModel } from './select-items.model';

@Component({
  selector: 'mja-select-items',
  templateUrl: './select-items.component.html',
  styleUrls: ['./select-items.component.scss'],
})
export class SelectItemsComponent implements OnInit, OnDestroy {

  #modelSubscription: Subscription = new Subscription();

  @Input()
  model!: SelectItemsCompomentModel;

  @Output()
  private selectedItemsChanged: EventEmitter<SelectItemsCompomentModel> = new EventEmitter<SelectItemsCompomentModel>();

  constructor(public selectItemsFacade: SelectItemsFacade) { }

  ngOnInit(): void {
    this.selectItemsFacade.init(this.model);
    this.#modelSubscription = this.selectItemsFacade.selectableItemsModel$.subscribe(
      model => this.selectedItemsChanged.emit(model)
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
