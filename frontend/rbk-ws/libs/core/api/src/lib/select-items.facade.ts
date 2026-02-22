import { Injectable } from '@angular/core';
import {
  initialSelectItemsComponentModel,
  SelectableItem,
  SelectItemsComponentModel,
  sortByName,
} from '@rbk-ws/core/model';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SelectItemsFacade {
  #model: SelectItemsComponentModel = initialSelectItemsComponentModel;
  #selectableItemsModelSubject = new BehaviorSubject<SelectItemsComponentModel>(initialSelectItemsComponentModel);

  #initialVorrat: SelectableItem[] = [];

  selectableItemsModel$: Observable<SelectItemsComponentModel> = this.#selectableItemsModelSubject.asObservable();

  init(model: SelectItemsComponentModel): void {
    this.#initialVorrat = model.vorrat;
    const restliste: SelectableItem[] = this.#getDifferenzmenge(model.vorrat, model.gewaehlteItems);
    this.#model = { ...model, vorrat: restliste };
    this.#fireModelChanged(this.#model);
  }

  resetSelection(): void {
    this.#model = { ...initialSelectItemsComponentModel, vorrat: this.#initialVorrat };
    this.#fireModelChanged(this.#model);
  }

  addToGewaehlt(theItem: SelectableItem): void {
    const vorrat: SelectableItem[] = [];
    const gewaehlt: SelectableItem[] = [...this.#model.gewaehlteItems, theItem];

    this.#model.vorrat.forEach(item => {
      if (theItem.id !== item.id) {
        vorrat.push(item);
      }
    });

    this.#model = { ...this.#model, vorrat: sortByName(vorrat), gewaehlteItems: sortByName(gewaehlt) };
    this.#fireModelChanged(this.#model);
  }

  removeFromGewaehlt(theItem: SelectableItem): void {
    const vorrat: SelectableItem[] = [...this.#model.vorrat, theItem];
    const gewaehlt: SelectableItem[] = [];

    this.#model.gewaehlteItems.forEach(item => {
      if (theItem.id !== item.id) {
        gewaehlt.push(item);
      }
    });

    this.#model = { ...this.#model, vorrat: sortByName(vorrat), gewaehlteItems: sortByName(gewaehlt) };
    this.#fireModelChanged(this.#model);
  }

  #fireModelChanged(model: SelectItemsComponentModel): void {
    this.#selectableItemsModelSubject.next(model);
  }

  #getDifferenzmenge(alle: SelectableItem[], auszuschliessen: SelectableItem[]): SelectableItem[] {
    const result = alle.filter(ele => {
      return !auszuschliessen.includes(ele);
    });

    return sortByName(result);
  }
}
