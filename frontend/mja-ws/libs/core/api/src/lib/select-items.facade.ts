import { Injectable } from "@angular/core";
import { initialSelectItemsComponentModel, SelectableItem, SelectItemsCompomentModel } from "@mja-ws/core/model";
import { BehaviorSubject, Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class SelectItemsFacade {

    #model: SelectItemsCompomentModel = initialSelectItemsComponentModel;
    #selectableItemsModelSubject = new BehaviorSubject<SelectItemsCompomentModel>(initialSelectItemsComponentModel);

    selectableItemsModel$: Observable<SelectItemsCompomentModel> = this.#selectableItemsModelSubject.asObservable();

    init(model: SelectItemsCompomentModel): void {
        const restliste: SelectableItem[] = this.#getDifferenzmenge(model.vorrat, model.gewaehlteItems);
        this.#model = { ...model, vorrat: restliste };
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

        this.#model = { ...this.#model, vorrat: vorrat, gewaehlteItems: gewaehlt };
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

        this.#model = { ...this.#model, vorrat: vorrat, gewaehlteItems: gewaehlt };
        this.#fireModelChanged(this.#model);
    }

    #fireModelChanged(model: SelectItemsCompomentModel): void {
        this.#selectableItemsModelSubject.next(model);
    }

    #getDifferenzmenge(alle: SelectableItem[], auszuschliessen: SelectableItem[]): SelectableItem[] {

        const result = alle.filter((ele) => {
            return !auszuschliessen.includes(ele)
        });

        return result;
    };


}
