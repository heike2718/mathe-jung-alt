import { _isTestEnvironment } from "@angular/cdk/platform";
import { Injectable } from "@angular/core";
import { MjaSetUtils, SelectableItem } from "@mja-workspace/shared/util-mja";
import { BehaviorSubject, map, Observable } from "rxjs";
import { initialSelectItemsComponentModel, SelectItemsCompomentModel } from "./select-items.model";


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

        const setUtils: MjaSetUtils<SelectableItem> = new MjaSetUtils();
        const result: SelectableItem[] = setUtils.getDifferenzmenge(setUtils.toMjaEntityArray(alle), setUtils.toMjaEntityArray(auszuschliessen));
        return result;
    };
}