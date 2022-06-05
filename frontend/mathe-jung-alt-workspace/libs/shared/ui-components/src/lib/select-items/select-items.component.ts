import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { SelectableItem, SelectableItemsComponentModel } from './select-items.model';

@Component({
  selector: 'mja-select-items',
  templateUrl: './select-items.component.html',
  styleUrls: ['./select-items.component.scss']
})
export class SelectItemsComponent implements OnInit {

  #modelSubject: BehaviorSubject<SelectableItemsComponentModel> = new BehaviorSubject<SelectableItemsComponentModel>({ vorrat: [], auswahl: [] });
  
  model$: Observable<SelectableItemsComponentModel> = this.#modelSubject.asObservable();

  #model: SelectableItemsComponentModel = { vorrat: [], auswahl: [] };

  @Input()
  itemList: SelectableItem[] = [];

  @Output()
  selectedItemsChanged: EventEmitter<SelectableItem[]> = new EventEmitter<SelectableItem[]>();

  // vorrat: SelectableItem[] = [];
  // auswahl: SelectableItem[] = [];

  constructor() { }

  ngOnInit(): void {
    const vorrat = this.itemList.filter(item => !item.selected);
    const auswahl = this.itemList.filter(item => item.selected);

    this.#model = {vorrat, auswahl};
    this.#modelSubject.next(this.#model);
  }

  addToAuswahl($item: SelectableItem): void {

    const selectedItem = { ...$item, selected: true };
    const auswahl: SelectableItem[] = [...this.#model.auswahl];
    auswahl.push(selectedItem);
    
    const vorrat: SelectableItem[] = [...this.#model.vorrat].filter(item => item.id !== selectedItem.id);
    this.#model = {...this.model$, vorrat: vorrat, auswahl: auswahl};
    this.propagateModelChanged();
  }

  removeFromAuswahl($item: SelectableItem): void {
    
    const selectedItem = { ...$item, selected: false };
    const auswahl: SelectableItem[] = [...this.#model.auswahl].filter(item => item.id !== selectedItem.id);

    const vorrat: SelectableItem[] = [...this.#model.vorrat];
    vorrat.push(selectedItem);    
    
    this.#model = {...this.model$, vorrat: vorrat, auswahl: auswahl};
    this.propagateModelChanged();
  }

  private propagateModelChanged(): void {
    this.#modelSubject.next(this.#model);
    this.selectedItemsChanged.emit(this.#model.auswahl);
  }

}
