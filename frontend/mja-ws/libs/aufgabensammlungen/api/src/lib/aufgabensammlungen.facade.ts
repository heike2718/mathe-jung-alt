import { inject, Injectable } from "@angular/core";
import { FontName, GeneratedImages, LaTeXLayoutAntwortvorschlaege, PageDefinition, PaginationState, Schriftgroesse, User } from "@mja-ws/core/model";
import { fromAufgabensammlungen, aufgabensammlungenActions } from "@mja-ws/aufgabensammlungen/data";
import { EditAufgabensammlungselementPayload, EditAufgabensammlungPayload, AufgabensammlungBasisdaten, AufgabensammlungDetails, Aufgabensammlungselement, AufgabensammlungenSuchparameter, AufgabensammlungTrefferItem, initialAufgabensammlungDetails } from "@mja-ws/aufgabensammlungen/model";
import { filterDefined } from "@mja-ws/shared/util";
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
    editorContent$: Observable<AufgabensammlungBasisdaten> = this.#store.select(fromAufgabensammlungen.aufgabensammlungBasisdaten).pipe(filterDefined);
    aufgabensammlungDetails$: Observable<AufgabensammlungDetails> = this.#store.select(fromAufgabensammlungen.aufgabensammlungDetails).pipe(filterDefined);
    aufgabensammlungBasisdaten$: Observable<AufgabensammlungBasisdaten> = this.#store.select(fromAufgabensammlungen.aufgabensammlungBasisdaten).pipe(filterDefined);
    aufgabensammlungselemente$: Observable<Aufgabensammlungselement[]> = this.#store.select(fromAufgabensammlungen.aufgabensammlungselemente);
    selectedAufgabensammlungselement$: Observable<Aufgabensammlungselement> = this.#store.select(fromAufgabensammlungen.selectedAufgabensammlungselement).pipe(filterDefined);
    selectedElementImages$: Observable<GeneratedImages | undefined> = this.#store.select(fromAufgabensammlungen.selectedElementImages);



    triggerSearch(aufgabensammlungenSuchparameter: AufgabensammlungenSuchparameter, pageDefinition: PageDefinition): void {
        this.#store.dispatch(aufgabensammlungenActions.aUFGABENSAMMLUNGEN_SELECT_PAGE({ pageDefinition }));
        this.#store.dispatch(aufgabensammlungenActions.fIND_AUFGABENSAMMLUNGEN({ aufgabensammlungenSuchparameter, pageDefinition }));
    }

    selectAufgabensammlung(aufgabensammlungId: string): void {

        this.#store.dispatch(aufgabensammlungenActions.sELECT_AUFGABENSAMMLUNG({ aufgabensammlungId }));
    }

    unselectAufgabensammlung(): void {
        this.#store.dispatch(aufgabensammlungenActions.uNSELECT_AUFGABENSAMMLUNG());
        this.#router.navigateByUrl('aufgabensammlungen/uebersicht')
    }

    selectAufgabensammlungselement(element: Aufgabensammlungselement): void {
        this.#store.dispatch(aufgabensammlungenActions.sELECT_AUFGABENSAMMLUNGSELEMENT({ aufgabensammlungselement: element }));
    }

    generiereArbeitsblatt(aufgabensammlungID: string, font: FontName, schriftgroesse: Schriftgroesse, layoutAntwortvorschlaege: LaTeXLayoutAntwortvorschlaege): void {
        this.#store.dispatch(aufgabensammlungenActions.gENERIERE_ARBEITSBLATT({ aufgabensammlungID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereKnobelkartei(aufgabensammlungID: string, font: FontName, schriftgroesse: Schriftgroesse, layoutAntwortvorschlaege: LaTeXLayoutAntwortvorschlaege): void {
        this.#store.dispatch(aufgabensammlungenActions.gENERIERE_KNOBELKARTEI({ aufgabensammlungID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereVorschau(aufgabensammlungID: string, font: FontName, schriftgroesse: Schriftgroesse, layoutAntwortvorschlaege: LaTeXLayoutAntwortvorschlaege): void {
        this.#store.dispatch(aufgabensammlungenActions.gENERIERE_VORSCHAU({ aufgabensammlungID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    generiereLaTeX(aufgabensammlungID: string, font: FontName, schriftgroesse: Schriftgroesse, layoutAntwortvorschlaege: LaTeXLayoutAntwortvorschlaege): void {
        this.#store.dispatch(aufgabensammlungenActions.gENERIERE_LATEX({ aufgabensammlungID, font, schriftgroesse, layoutAntwortvorschlaege }));
    }

    createAndEditAufgabensammlung(user: User): void {
        const privat = user.isAdmin ? false : true;
        this.editAufgabensammlung({ ...initialAufgabensammlungDetails, privat: privat });
    }

    editAufgabensammlung(augabensammlungDetails: AufgabensammlungDetails): void {

        const aufgabensammlung: AufgabensammlungBasisdaten = {
            freigegeben: augabensammlungDetails.freigegeben,
            geaendertDurch: augabensammlungDetails.geaendertDurch,
            id: augabensammlungDetails.id,
            kommentar: augabensammlungDetails.kommentar,
            name: augabensammlungDetails.name,
            privat: augabensammlungDetails.privat,
            referenz: augabensammlungDetails.referenz,
            referenztyp: augabensammlungDetails.referenztyp,
            schwierigkeitsgrad: augabensammlungDetails.schwierigkeitsgrad
        }

        this.#store.dispatch(aufgabensammlungenActions.eDIT_AUFGABENSAMMLUNG({ aufgabensammlung }));
    }

    reloadAufgabensammlung(aufgabensammlung: AufgabensammlungDetails): void {       

        this.selectAufgabensammlung(aufgabensammlung.id);
    }

    toggleStatus(aufgabensammlung: AufgabensammlungDetails): void {

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
            this.#store.dispatch(aufgabensammlungenActions.sELECT_AUFGABENSAMMLUNG({ aufgabensammlungId: aufgabensammlung.id }));
        }
    }

    saveAufgabensammlungselement(aufgabensammlungID: string, payload: EditAufgabensammlungselementPayload): void {
        this.#store.dispatch(aufgabensammlungenActions.sAVE_AUFGABENSAMMLUNGSELEMENT({ aufgabensammlungID, payload }));
    }

    deleteAufgabensammlungselement(aufgabensammlungID: string, payload: Aufgabensammlungselement): void {
        this.#store.dispatch(aufgabensammlungenActions.dELETE_AUFGABENSAMMLUNGSELEMENT({ aufgabensammlungID, payload }));
    }
}