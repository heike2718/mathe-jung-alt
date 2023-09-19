import { inject, Injectable } from "@angular/core";
import { FONT_NAME, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, PaginationState, SCHRIFTGROESSE, STATUS } from "@mja-ws/core/model";
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
        this.#store.dispatch(raetselgruppenActions.rAETSELGRUPPEN_SELECT_PAGE({ pageDefinition }));
        this.#store.dispatch(raetselgruppenActions.fIND_RAETSELGRUPPEN({ raetselgruppenSuchparameter, pageDefinition }));
    }

    selectRaetselgruppe(raetselgruppe: RaetselgruppenTrefferItem): void {

        this.#store.dispatch(raetselgruppenActions.sELECT_RAETSELGRUPPE({ raetselgruppe }));
    }

    unselectRaetselgruppe(): void {
        this.#store.dispatch(raetselgruppenActions.uNSELECT_RAETSELGRUPPE());
    }

    generiereArbeitsblatt(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE): void {
        this.#store.dispatch(raetselgruppenActions.gENERIERE_ARBEITSBLATT({ raetselgruppeID, font, schriftgroesse }));
    }

    generiereKnobelkartei(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE): void {
        this.#store.dispatch(raetselgruppenActions.gENERIERE_KNOBELKARTEI({ raetselgruppeID, font, schriftgroesse }));
    }

    generiereVorschau(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(raetselgruppenActions.gENERIERE_VORSCHAU({ raetselgruppeID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereLaTeX(raetselgruppeID: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(raetselgruppenActions.gENERIERE_LATEX({ raetselgruppeID, layoutAntwortvorschlaege }));
    }

    createAndEditRaetselgruppe(): void {
        this.editRaetselgruppe(initialRaetselgruppeBasisdaten);
    }

    editRaetselgruppe(raetselgruppeBasisdaten: RaetselgruppeBasisdaten): void {
        this.#store.dispatch(raetselgruppenActions.eDIT_RAETSELGUPPE({ raetselgruppeBasisdaten }));
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
        this.#store.dispatch(raetselgruppenActions.sAVE_RAETSELGRUPPE({ editRaetselgruppePayload }));
    }

    cancelEdit(raetselgruppe: RaetselgruppeBasisdaten): void {

        if (raetselgruppe.id === 'neu') {
            this.#store.dispatch(raetselgruppenActions.uNSELECT_RAETSELGRUPPE());
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
            this.#store.dispatch(raetselgruppenActions.sELECT_RAETSELGRUPPE({ raetselgruppe: rg }));
        }
    }

    saveRaetselgruppenelement(raetselgruppeID: string, payload: EditRaetselgruppenelementPayload): void {
        this.#store.dispatch(raetselgruppenActions.sAVE_RAETSELGRUPPENELEMENT({ raetselgruppeID, payload }));
    }

    deleteRaetselgruppenelement(raetselgruppeID: string, payload: Raetselgruppenelement): void {
        this.#store.dispatch(raetselgruppenActions.dELETE_RAETSELGRUPPENELEMENT({ raetselgruppeID, payload }));
    }
}