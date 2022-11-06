import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import * as fromRaetselgruppen from '../+state/raetselgruppen.reducer';
import * as RaetselgruppenActions from '../+state/raetselgruppen.actions';
import * as RaetselgruppenSelectors from '../+state/raetselgruppen.selectors';
import { EditRaetselgruppenelementPayload, EditRaetselgruppePayload, initialRaetselgruppeBasisdaten, RaetselgruppeBasisdaten, RaetselgruppeDetails, RaetselgruppensucheTreffer, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";
import { Observable } from "rxjs";
import { Router } from "@angular/router";
import { LATEX_LAYOUT_ANTWORTVORSCHLAEGE } from "@mja-workspace/raetsel/domain";

@Injectable({
    providedIn: 'root'
})
export class RaetselgruppenFacade {

    page$ = this.store.pipe(select(RaetselgruppenSelectors.getPage));
    anzahlTrefferGesamt$ = this.store.pipe(select(RaetselgruppenSelectors.getAnzahlTrefferGesamt));
    suchparameter$: Observable<RaetselgruppenSuchparameter> = this.store.pipe(select(RaetselgruppenSelectors.getRaetselgruppenSuchparameter));
    editorContent$ = this.store.pipe(select(RaetselgruppenSelectors.getRaetselgruppeBasisdaten));
    raetselgruppeDetails$: Observable<RaetselgruppeDetails | undefined> = this.store.pipe(select(RaetselgruppenSelectors.getRaetselgruppeDetails));
    raetselgruppenelemente$ = this.store.pipe(select(RaetselgruppenSelectors.getRaetselgruppenelemente));

    constructor(private store: Store<fromRaetselgruppen.RaetselgruppenPartialState>,
        private router: Router) { }

    public setSuchparameter(suchparameter: RaetselgruppenSuchparameter): void {
        this.store.dispatch(RaetselgruppenActions.suchparameterChanged({ suchparameter }));
    }

    public selectRaetselgruppe(raetselgruppe: RaetselgruppensucheTrefferItem): void {
        this.store.dispatch(RaetselgruppenActions.selectRaetselgruppe({ raetselgruppe }));
    }

    public cancelEdit(raetselgruppe: RaetselgruppeBasisdaten): void {

        if (raetselgruppe.id === 'neu') {
            this.router.navigateByUrl('/raetselgruppen/uebersicht');
        } else {
            const rg: RaetselgruppensucheTrefferItem = {
                anzahlElemente: 0,
                id: raetselgruppe.id,
                schwierigkeitsgrad: raetselgruppe.schwierigkeitsgrad,
                status: raetselgruppe.status,
                referenztyp: raetselgruppe.referenztyp,
                referenz: raetselgruppe.referenz
            };
            this.store.dispatch(RaetselgruppenActions.selectRaetselgruppe({ raetselgruppe: rg }));
        }        
    }

    public unselectRaetselgruppe(): void {
        this.store.dispatch(RaetselgruppenActions.unselectRaetselgruppe());
    }

    public createAndEditRaetselgruppe(): void {
        this.editRaetselgruppe(initialRaetselgruppeBasisdaten);
    }

    public editRaetselgruppe(raetselgruppeBasisdaten: RaetselgruppeBasisdaten): void {
        this.store.dispatch(RaetselgruppenActions.editRaetselgruppe({ raetselgruppeBasisdaten }));
    }

    public saveRaetselgruppe(editRaetselgruppePayload: EditRaetselgruppePayload): void {
        this.store.dispatch(RaetselgruppenActions.startSaveRaetselgruppe({ editRaetselgruppePayload }));
    }

    public saveRaetselgruppenelement(raetselgruppeID: string, payload: EditRaetselgruppenelementPayload): void {
        this.store.dispatch(RaetselgruppenActions.saveRaetselgruppenelement({ raetselgruppeID, payload }));
    }

    public deleteRaetselgruppenelement(raetselgruppeID: string, payload: EditRaetselgruppenelementPayload): void {
        this.store.dispatch(RaetselgruppenActions.deleteRaetselgruppenelement({ raetselgruppeID, payload }));
    }

    public generiereVorschau(raetselgruppeID: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {
        this.store.dispatch(RaetselgruppenActions.generiereVorschau({raetselgruppeID, layoutAntwortvorschlaege}));
    }
}
