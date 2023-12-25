import { inject, Injectable } from "@angular/core";
import { FONT_NAME, GeneratedImages, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, PaginationState, SCHRIFTGROESSE } from "@mja-ws/core/model";
import { fromAufgabensammlungen, aufgabensammlungenActions } from "@mja-ws/aufgabensammlungen/data";
import { EditAufgabensammlungselementPayload, EditAufgabensammlungPayload, initialAufgabensammlungBasisdaten, AufgabensammlungBasisdaten, AufgabensammlungDetails, Aufgabensammlungselement, AufgabensammlungenSuchparameter, AufgabensammlungTrefferItem } from "@mja-ws/aufgabensammlungen/model";
import { deepClone, filterDefined } from "@mja-ws/shared/util";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { Router } from "@angular/router";


@Injectable({ providedIn: 'root' })
export class AufgabensammlungenFacade {

    #store = inject(Store);
    #router = inject(Router);

    page$: Observable<AufgabensammlungTrefferItem[]> = this.#store.select(fromAufgabensammlungen.page);
    anzahlTrefferGesamt$: Observable<number> = this.#store.select(fromAufgabensammlungen.anzahlTrefferGesamt);
    paginationState$: Observable<PaginationState> = this.#store.select(fromAufgabensammlungen.paginationState);
    editorContent$: Observable<AufgabensammlungBasisdaten> = this.#store.select(fromAufgabensammlungen.aufgabensammlungBasisdaten).pipe(filterDefined, deepClone);
    aufgabensammlungDetails$: Observable<AufgabensammlungDetails> = this.#store.select(fromAufgabensammlungen.aufgabensammlungDetails).pipe(filterDefined, deepClone);
    aufgabensammlungBasisdaten$: Observable<AufgabensammlungBasisdaten> = this.#store.select(fromAufgabensammlungen.aufgabensammlungBasisdaten).pipe(filterDefined, deepClone);
    aufgabensammlungselemente$: Observable<Aufgabensammlungselement[]> = this.#store.select(fromAufgabensammlungen.aufgabensammlungselemente);
    selectedAufgabensammlungselement$: Observable<Aufgabensammlungselement> = this.#store.select(fromAufgabensammlungen.selectedAufgabensammlungselement).pipe(filterDefined, deepClone);
    selectedElementImages$: Observable<GeneratedImages | undefined> = this.#store.select(fromAufgabensammlungen.selectedElementImages);

    triggerSearch(aufgabensammlungenSuchparameter: AufgabensammlungenSuchparameter, pageDefinition: PageDefinition): void {
        this.#store.dispatch(aufgabensammlungenActions.aUFGABENSAMMLUNGEN_SELECT_PAGE({ pageDefinition }));
        this.#store.dispatch(aufgabensammlungenActions.fIND_AUFGABENSAMMLUNGEN({ aufgabensammlungenSuchparameter, pageDefinition }));
    }

    selectAufgabensammlung(aufgabensammlung: AufgabensammlungTrefferItem): void {

        this.#store.dispatch(aufgabensammlungenActions.sELECT_AUFGABENSAMMLUNG({ aufgabensammlung }));
    }

    unselectAufgabensammlung(): void {
        this.#store.dispatch(aufgabensammlungenActions.uNSELECT_AUFGABENSAMMLUNG());
        this.#router.navigateByUrl('aufgabensammlungen/uebersicht')
    }

    selectAufgabensammlungselement(element: Aufgabensammlungselement): void {
        this.#store.dispatch(aufgabensammlungenActions.sELECT_AUFGABENSAMMLUNGSELEMENT({ aufgabensammlungselement: element }));
    }

    generiereArbeitsblatt(aufgabensammlungID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(aufgabensammlungenActions.gENERIERE_ARBEITSBLATT({ aufgabensammlungID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereKnobelkartei(aufgabensammlungID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(aufgabensammlungenActions.gENERIERE_KNOBELKARTEI({ aufgabensammlungID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereVorschau(aufgabensammlungID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(aufgabensammlungenActions.gENERIERE_VORSCHAU({ aufgabensammlungID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereLaTeX(aufgabensammlungID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.#store.dispatch(aufgabensammlungenActions.gENERIERE_LATEX({ aufgabensammlungID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    createAndEditAufgabensammlung(): void {
        this.editAufgabensammlung(initialAufgabensammlungBasisdaten);
    }

    editAufgabensammlung(aufgabensammlung: AufgabensammlungBasisdaten): void {
        this.#store.dispatch(aufgabensammlungenActions.eDIT_AUFGABENSAMMLUNG({ aufgabensammlungBasisdaten: aufgabensammlung }));
    }

    reloadAufgabensammlung(aufgabensammlung: AufgabensammlungBasisdaten, anzahlElemente: number): void {

        const theAufgabensammlung: AufgabensammlungTrefferItem = {
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

        this.selectAufgabensammlung(theAufgabensammlung);
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

        this.saveAufgabensammlung(EditAufgabensammlungPayload);
    }

    saveAufgabensammlung(editAufgabensammlungPayload: EditAufgabensammlungPayload): void {
        this.#store.dispatch(aufgabensammlungenActions.sAVE_AUFGABENSAMMLUNG({ editAufgabensammlungPayload }));
    }

    cancelEdit(aufgabensammlung: AufgabensammlungBasisdaten): void {

        if (aufgabensammlung.id === 'neu') {
            this.#store.dispatch(aufgabensammlungenActions.uNSELECT_AUFGABENSAMMLUNG());
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
            this.#store.dispatch(aufgabensammlungenActions.sELECT_AUFGABENSAMMLUNG({ aufgabensammlung: rg }));
        }
    }

    saveAufgabensammlungselement(aufgabensammlungID: string, payload: EditAufgabensammlungselementPayload): void {
        this.#store.dispatch(aufgabensammlungenActions.sAVE_AUFGABENSAMMLUNGSELEMENT({ aufgabensammlungID, payload }));
    }

    deleteAufgabensammlungselement(aufgabensammlungID: string, payload: Aufgabensammlungselement): void {
        this.#store.dispatch(aufgabensammlungenActions.dELETE_AUFGABENSAMMLUNGSELEMENT({ aufgabensammlungID, payload }));
    }
}