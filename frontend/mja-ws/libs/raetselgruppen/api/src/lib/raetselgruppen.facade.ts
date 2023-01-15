import { inject, Injectable } from "@angular/core";
import { PageDefinition, PaginationState } from "@mja-ws/core/model";
import { fromRaetselgruppen, raetselgruppenActions } from "@mja-ws/raetselgruppen/data";
import { RaetselgruppenSuchparameter, RaetselgruppenTrefferItem } from "@mja-ws/raetselgruppen/model";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";


@Injectable({providedIn: 'root'})
export class RaetselgruppenFacade {

    #store = inject(Store);

    page$: Observable<RaetselgruppenTrefferItem[]> = this.#store.select(fromRaetselgruppen.page);
    anzahlTrefferGesamt$: Observable<number> = this.#store.select(fromRaetselgruppen.anzahlTrefferGesamt);
    paginationState$: Observable<PaginationState> = this.#store.select(fromRaetselgruppen.paginationState);

    triggerSearch(raetselgruppenSuchparameter: RaetselgruppenSuchparameter, pageDefinition: PageDefinition): void {
        this.#store.dispatch(raetselgruppenActions.raetselgruppen_select_page({pageDefinition}));
        this.#store.dispatch(raetselgruppenActions.find_raetselgruppen({raetselgruppenSuchparameter, pageDefinition}));
    }
}