import { Component, EventEmitter, inject, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatChipsModule } from '@angular/material/chips';
import { MatListModule } from '@angular/material/list';
import { Subscription } from 'rxjs';
import { SelectItemsFacade } from '@mja-ws/core/api';
import { SelectableItem, SelectItemsCompomentModel } from '@mja-ws/core/model';

@Component({
  selector: 'mja-select-items',
  standalone: true,
  imports: [
    CommonModule,
    MatChipsModule,
    MatListModule
  ],
  templateUrl: './select-items.component.html',
  styleUrls: ['./select-items.component.scss'],
})
export class SelectItemsComponent implements OnInit, OnDestroy {

  #modelSubscription: Subscription = new Subscription();

  selectItemsFacade = inject(SelectItemsFacade);

  @Input()
  model!: SelectItemsCompomentModel;

  @Output()
  private selectedItemsChanged: EventEmitter<SelectItemsCompomentModel> = new EventEmitter<SelectItemsCompomentModel>();

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
