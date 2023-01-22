import { inject, Injectable } from "@angular/core";
import { LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, PaginationState } from "@mja-ws/core/model";
import { fromRaetselgruppen, raetselgruppenActions } from "@mja-ws/raetselgruppen/data";
import { EditRaetselgruppenelementPayload, RaetselgruppeDetails, Raetselgruppenelement, RaetselgruppenSuchparameter, RaetselgruppenTrefferItem } from "@mja-ws/raetselgruppen/model";
import { deepClone, filterDefined } from "@mja-ws/shared/ngrx-utils";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";


@Injectable({ providedIn: 'root' })
export class RaetselgruppenFacade {

    #store = inject(Store);

    page$: Observable<RaetselgruppenTrefferItem[]> = this.#store.select(fromRaetselgruppen.page);
    anzahlTrefferGesamt$: Observable<number> = this.#store.select(fromRaetselgruppen.anzahlTrefferGesamt);
    paginationState$: Observable<PaginationState> = this.#store.select(fromRaetselgruppen.paginationState);
    raetselgruppeDetails$: Observable<RaetselgruppeDetails> = this.#store.select(fromRaetselgruppen.raetselgruppeDetails).pipe(filterDefined, deepClone);
    raetselgruppenelemente$: Observable<Raetselgruppenelement[]> = this.#store.select(fromRaetselgruppen.raetselgruppenelemente);

    triggerSearch(raetselgruppenSuchparameter: RaetselgruppenSuchparameter, pageDefinition: PageDefinition): void {
        this.#store.dispatch(raetselgruppenActions.raetselgruppen_select_page({ pageDefinition }));
        this.#store.dispatch(raetselgruppenActions.find_raetselgruppen({ raetselgruppenSuchparameter, pageDefinition }));
    }

    selectRaetselgruppe(raetselgruppe: RaetselgruppenTrefferItem): void {

        this.#store.dispatch(raetselgruppenActions.select_raetselgruppe({ raetselgruppe }));
    }

    unselectRaetselgruppe(): void {
        this.#store.dispatch(raetselgruppenActions.unselect_raetselgruppe());
    }

    generiereVorschau(raetselgruppeID: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(raetselgruppenActions.generiere_vorschau({raetselgruppeID, layoutAntwortvorschlaege}));
    }

    generiereLaTeX(raetselgruppeID: string): void {
        this.#store.dispatch(raetselgruppenActions.generiere_latex({raetselgruppeID}));
    }

    saveRaetselgruppenelement(raetselgruppeID: string, payload: EditRaetselgruppenelementPayload): void {
        this.#store.dispatch(raetselgruppenActions.save_raetselgruppenelement({raetselgruppeID, payload}));
    }

    deleteRaetselgruppenelement(raetselgruppeID: string, payload: Raetselgruppenelement): void {
        this.#store.dispatch(raetselgruppenActions.delete_raetselgruppenelement({raetselgruppeID, payload}));
    }
}