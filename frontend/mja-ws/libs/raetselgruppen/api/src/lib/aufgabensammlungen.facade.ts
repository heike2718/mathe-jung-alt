import { inject, Injectable } from "@angular/core";
import { FONT_NAME, GeneratedImages, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, PaginationState, SCHRIFTGROESSE } from "@mja-ws/core/model";
import { fromRaetselgruppen, raetselgruppenActions } from "@mja-ws/raetselgruppen/data";
import { EditAufgabensammlungselementPayload, EditAufgabensammlungPayload, initialAufgabensammlungBasisdaten, AufgabensammlungBasisdaten, AufgabensammlungDetails, Aufgabensammlungselement, AufgabensammlungenSuchparameter, AufgabensammlungTrefferItem } from "@mja-ws/raetselgruppen/model";
import { deepClone, filterDefined } from "@mja-ws/shared/ngrx-utils";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";


@Injectable({ providedIn: 'root' })
export class RaetselgruppenFacade {

    #store = inject(Store);

    page$: Observable<AufgabensammlungTrefferItem[]> = this.#store.select(fromRaetselgruppen.page);
    anzahlTrefferGesamt$: Observable<number> = this.#store.select(fromRaetselgruppen.anzahlTrefferGesamt);
    paginationState$: Observable<PaginationState> = this.#store.select(fromRaetselgruppen.paginationState);
    editorContent$: Observable<AufgabensammlungBasisdaten> = this.#store.select(fromRaetselgruppen.aufgabensammlungBasisdaten).pipe(filterDefined, deepClone);
    aufgabensammlungDetails$: Observable<AufgabensammlungDetails> = this.#store.select(fromRaetselgruppen.aufgabensammlungDetails).pipe(filterDefined, deepClone);
    aufgabensammlungBasisdaten$: Observable<AufgabensammlungBasisdaten> = this.#store.select(fromRaetselgruppen.aufgabensammlungBasisdaten).pipe(filterDefined, deepClone);
    aufgabensammlungselemente$: Observable<Aufgabensammlungselement[]> = this.#store.select(fromRaetselgruppen.aufgabensammlungselemente);
    selectedAufgabensammlungselement$: Observable<Aufgabensammlungselement> = this.#store.select(fromRaetselgruppen.selectedAufgabensammlungselement).pipe(filterDefined, deepClone);
    selectedElementImages$: Observable<GeneratedImages | undefined> = this.#store.select(fromRaetselgruppen.selectedElementImages);

    triggerSearch(aufgabensammlungenSuchparameter: AufgabensammlungenSuchparameter, pageDefinition: PageDefinition): void {
        this.#store.dispatch(raetselgruppenActions.rAETSELGRUPPEN_SELECT_PAGE({ pageDefinition }));
        this.#store.dispatch(raetselgruppenActions.fIND_RAETSELGRUPPEN({ aufgabensammlungenSuchparameter, pageDefinition }));
    }

    selectRaetselgruppe(raetselgruppe: AufgabensammlungTrefferItem): void {

        this.#store.dispatch(raetselgruppenActions.sELECT_RAETSELGRUPPE({ raetselgruppe }));
    }

    unselectRaetselgruppe(): void {
        this.#store.dispatch(raetselgruppenActions.uNSELECT_RAETSELGRUPPE());
    }

    selectAufgabensammlungselement(element: Aufgabensammlungselement): void {
        this.#store.dispatch(raetselgruppenActions.sELECT_RAETSELGRUPPENELEMENT({ aufgabensammlungselement: element }));
    }

    generiereArbeitsblatt(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(raetselgruppenActions.gENERIERE_ARBEITSBLATT({ raetselgruppeID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereKnobelkartei(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(raetselgruppenActions.gENERIERE_KNOBELKARTEI({ raetselgruppeID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereVorschau(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(raetselgruppenActions.gENERIERE_VORSCHAU({ raetselgruppeID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereLaTeX(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(raetselgruppenActions.gENERIERE_LATEX({ raetselgruppeID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    createAndEditRaetselgruppe(): void {
        this.editRaetselgruppe(initialAufgabensammlungBasisdaten);
    }

    editRaetselgruppe(aufgabensammlung: AufgabensammlungBasisdaten): void {
        this.#store.dispatch(raetselgruppenActions.eDIT_RAETSELGUPPE({ aufgabensammlungBasisdaten: aufgabensammlung }));
    }

    reloadRaetselgruppe(aufgabensammlung: AufgabensammlungBasisdaten, anzahlElemente: number): void {

        const raetselgruppe: AufgabensammlungTrefferItem = {
            anzahlElemente: anzahlElemente,
            geaendertDurch: aufgabensammlung.geaendertDurch,
            id: aufgabensammlung.id,
            name: aufgabensammlung.name,
            referenz: aufgabensammlung.referenz,
            referenztyp: aufgabensammlung.referenztyp,
            schwierigkeitsgrad: aufgabensammlung.schwierigkeitsgrad,
            freigegeben: aufgabensammlung.freigegeben,
            privat: aufgabensammlung.privat
        };

        this.selectRaetselgruppe(raetselgruppe);
    }

    toggleStatus(aufgabensammlung: AufgabensammlungBasisdaten): void {

        const EditAufgabensammlungPayload: EditAufgabensammlungPayload = {
            id: aufgabensammlung.id,
            kommentar: aufgabensammlung.kommentar,
            name: aufgabensammlung.name,
            referenz: aufgabensammlung.referenz,
            referenztyp: aufgabensammlung.referenztyp,
            schwierigkeitsgrad: aufgabensammlung.schwierigkeitsgrad,
            freigegeben: !aufgabensammlung.freigegeben,
            privat: aufgabensammlung.privat
        };

        this.saveRaetselgruppe(EditAufgabensammlungPayload);
    }

    saveRaetselgruppe(editAufgabensammlungPayload: EditAufgabensammlungPayload): void {
        this.#store.dispatch(raetselgruppenActions.sAVE_RAETSELGRUPPE({ editAufgabensammlungPayload }));
    }

    cancelEdit(aufgabensammlung: AufgabensammlungBasisdaten): void {

        if (aufgabensammlung.id === 'neu') {
            this.#store.dispatch(raetselgruppenActions.uNSELECT_RAETSELGRUPPE());
        } else {
            const rg: AufgabensammlungTrefferItem = {
                id: aufgabensammlung.id,
                name: '',
                schwierigkeitsgrad: aufgabensammlung.schwierigkeitsgrad,
                freigegeben: aufgabensammlung.freigegeben,
                privat: aufgabensammlung.privat,
                referenztyp: aufgabensammlung.referenztyp,
                referenz: aufgabensammlung.referenz,
                geaendertDurch: aufgabensammlung.geaendertDurch,
                anzahlElemente: 0
            };
            this.#store.dispatch(raetselgruppenActions.sELECT_RAETSELGRUPPE({ raetselgruppe: rg }));
        }
    }

    saveAufgabensammlungselement(raetselgruppeID: string, payload: EditAufgabensammlungselementPayload): void {
        this.#store.dispatch(raetselgruppenActions.sAVE_RAETSELGRUPPENELEMENT({ raetselgruppeID, payload }));
    }

    deleteAufgabensammlungselement(raetselgruppeID: string, payload: Aufgabensammlungselement): void {
        this.#store.dispatch(raetselgruppenActions.dELETE_RAETSELGRUPPENELEMENT({ raetselgruppeID, payload }));
    }
}