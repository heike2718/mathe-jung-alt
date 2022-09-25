import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import * as fromRaetselgruppen from '../+state/raetselgruppen.reducer';
import * as RaetselgruppenActions from '../+state/raetselgruppen.actions';
import * as RaetselgruppenSelectors from '../+state/raetselgruppen.selectors';
import { EditRaetselgruppenelementPayload, EditRaetselgruppePayload, initialRaetselgruppeBasisdaten, RaetselgruppeBasisdaten, RaetselgruppeDetails, RaetselgruppensucheTreffer, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";
import { Observable } from "rxjs";
import { SafeHttpService } from "@mja-workspace/shared/util-mja";
import { RaetselgruppenHttpService } from "../infrastructure/raetselgruppen.http.service";


@Injectable({
    providedIn: 'root'
})
export class RaetselgruppenFacade {

    page$ = this.store.pipe(select(RaetselgruppenSelectors.getPage));
    anzahlTrefferGesamt$ = this.store.pipe(select(RaetselgruppenSelectors.getAnzahlTrefferGesamt));
    suchparameter$: Observable<RaetselgruppenSuchparameter> = this.store.pipe(select(RaetselgruppenSelectors.getRaetselgruppenSuchparameter));
    selectedGruppe$ = this.store.pipe(select(RaetselgruppenSelectors.getSelectedGruppe));
    editorContent$ = this.store.pipe(select(RaetselgruppenSelectors.getRaetselgruppeBasisdaten));
    raetselgruppeDetails$: Observable<RaetselgruppeDetails | undefined> = this.store.pipe(select(RaetselgruppenSelectors.getRaetselgruppeDetails));
    raetselgruppenelemente$ = this.store.pipe(select(RaetselgruppenSelectors.getRaetselgruppenelemente));

    constructor(private store: Store<fromRaetselgruppen.RaetselgruppenPartialState>, private safeHttpService: SafeHttpService, private httpService: RaetselgruppenHttpService) { }

    public setSuchparameter(suchparameter: RaetselgruppenSuchparameter): void {
        this.store.dispatch(RaetselgruppenActions.suchparameterChanged({ suchparameter }));
    }

    public selectRaetselgruppe(raetselgruppe: RaetselgruppensucheTrefferItem): void {
        this.store.dispatch(RaetselgruppenActions.selectRaetselgruppe({ raetselgruppe }));
    }

    public createAndEditRaetselgruppe(): void {

        const raetselgruppeBasisdaten: RaetselgruppeBasisdaten = initialRaetselgruppeBasisdaten;
        this.store.dispatch(RaetselgruppenActions.editRaetselgruppe({ raetselgruppeBasisdaten }));
    }

    public saveRaetselgruppe(editRaetselgruppePayload: EditRaetselgruppePayload): void {
        this.store.dispatch(RaetselgruppenActions.startSaveRaetselgruppe({ editRaetselgruppePayload }));
    }

    public saveRaetselgruppenelement(raetselgruppeID: string, payload: EditRaetselgruppenelementPayload): void {
        this.store.dispatch(RaetselgruppenActions.saveRaetselgruppenelement({raetselgruppeID, payload}));
    }

    public deleteRaetselgruppenelement(raetselgruppeID: string, payload: EditRaetselgruppenelementPayload): void {
        this.store.dispatch(RaetselgruppenActions.deleteRaetselgruppenelement({raetselgruppeID, payload}));
    }
}
