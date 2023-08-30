import { inject, Injectable } from "@angular/core";
import { LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, PaginationState, STATUS } from "@mja-ws/core/model";
import { fromRaetselgruppen, raetselgruppenActions } from "@mja-ws/raetselgruppen/data";
import { EditRaetselgruppenelementPayload, EditRaetselgruppePayload, initialRaetselgruppeBasisdaten, RaetselgruppeBasisdaten, RaetselgruppeDetails, Raetselgruppenelement, RaetselgruppenSuchparameter, RaetselgruppenTrefferItem } from "@mja-ws/raetselgruppen/model";
import { FrageLoesungImagesComponent } from "@mja-ws/shared/components";
import { deepClone, filterDefined } from "@mja-ws/shared/ngrx-utils";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";


@Injectable({ providedIn: 'root' })
export class RaetselgruppenFacade {

    #store = inject(Store);

    page$: Observable<RaetselgruppenTrefferItem[]> = this.#store.select(fromRaetselgruppen.page);
    anzahlTrefferGesamt$: Observable<number> = this.#store.select(fromRaetselgruppen.anzahlTrefferGesamt);
    paginationState$: Observable<PaginationState> = this.#store.select(fromRaetselgruppen.paginationState);
    editorContent$: Observable<RaetselgruppeBasisdaten> = this.#store.select(fromRaetselgruppen.raetselgruppeBasisdaten).pipe(filterDefined, deepClone);
    raetselgruppeDetails$: Observable<RaetselgruppeDetails> = this.#store.select(fromRaetselgruppen.raetselgruppeDetails).pipe(filterDefined, deepClone);
    raetselgruppeBasisdaten$: Observable<RaetselgruppeBasisdaten> = this.#store.select(fromRaetselgruppen.raetselgruppeBasisdaten).pipe(filterDefined, deepClone);
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

    generiereVorschau(raetselgruppeID: string): void {
        this.#store.dispatch(raetselgruppenActions.generiere_vorschau({ raetselgruppeID }));
    }

    generiereLaTeX(raetselgruppeID: string): void {
        this.#store.dispatch(raetselgruppenActions.generiere_latex({ raetselgruppeID }));
    }

    createAndEditRaetselgruppe(): void {
        this.editRaetselgruppe(initialRaetselgruppeBasisdaten);
    }

    editRaetselgruppe(raetselgruppeBasisdaten: RaetselgruppeBasisdaten): void {
        this.#store.dispatch(raetselgruppenActions.edit_raetselguppe({ raetselgruppeBasisdaten }));
    }

    reloadRaetselgruppe(raetselgruppeBasisdaten: RaetselgruppeBasisdaten, anzahlElemente: number): void {

        const raetselgruppe: RaetselgruppenTrefferItem = {
            anzahlElemente: anzahlElemente,
            geaendertDurch: raetselgruppeBasisdaten.geaendertDurch,
            id: raetselgruppeBasisdaten.id,
            name: raetselgruppeBasisdaten.name,
            referenz: raetselgruppeBasisdaten.referenz,
            referenztyp: raetselgruppeBasisdaten.referenztyp,
            schwierigkeitsgrad: raetselgruppeBasisdaten.schwierigkeitsgrad,
            status: raetselgruppeBasisdaten.status
        };

        this.selectRaetselgruppe(raetselgruppe);
    }

    toggleStatus(raetselgruppeBasisdaten: RaetselgruppeBasisdaten): void {

        const editRaetselgruppePayload: EditRaetselgruppePayload = {
            id:raetselgruppeBasisdaten.id,
            kommentar: raetselgruppeBasisdaten.kommentar,
            name: raetselgruppeBasisdaten.name,
            referenz: raetselgruppeBasisdaten.referenz,
            referenztyp: raetselgruppeBasisdaten.referenztyp,
            schwierigkeitsgrad: raetselgruppeBasisdaten.schwierigkeitsgrad,
            status: raetselgruppeBasisdaten.status === "ERFASST" ? "FREIGEGEBEN" : "ERFASST"
        };

        this.saveRaetselgruppe(editRaetselgruppePayload);
    }

    saveRaetselgruppe(editRaetselgruppePayload: EditRaetselgruppePayload): void {
        this.#store.dispatch(raetselgruppenActions.save_raetselgruppe({ editRaetselgruppePayload }));
    }

    cancelEdit(raetselgruppe: RaetselgruppeBasisdaten): void {

        if (raetselgruppe.id === 'neu') {
            this.#store.dispatch(raetselgruppenActions.unselect_raetselgruppe());
        } else {
            const rg: RaetselgruppenTrefferItem = {
                id: raetselgruppe.id,
                name: '',
                schwierigkeitsgrad: raetselgruppe.schwierigkeitsgrad,
                status: raetselgruppe.status,
                referenztyp: raetselgruppe.referenztyp,
                referenz: raetselgruppe.referenz,
                geaendertDurch: raetselgruppe.geaendertDurch,
                anzahlElemente: 0
            };
            this.#store.dispatch(raetselgruppenActions.select_raetselgruppe({ raetselgruppe: rg }));
        }
    }

    saveRaetselgruppenelement(raetselgruppeID: string, payload: EditRaetselgruppenelementPayload): void {
        this.#store.dispatch(raetselgruppenActions.save_raetselgruppenelement({ raetselgruppeID, payload }));
    }

    deleteRaetselgruppenelement(raetselgruppeID: string, payload: Raetselgruppenelement): void {
        this.#store.dispatch(raetselgruppenActions.delete_raetselgruppenelement({ raetselgruppeID, payload }));
    }
}