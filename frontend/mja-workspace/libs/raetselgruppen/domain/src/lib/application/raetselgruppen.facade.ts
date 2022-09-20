import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import * as fromRaetselgruppen from '../+state/raetselgruppen.reducer';
import * as RaetselgruppenActions from '../+state/raetselgruppen.actions';
import * as RaetselgruppenSelectors from '../+state/raetselgruppen.selectors';
import { initialRaetselgruppeBasisdaten, RaetselgruppeBasisdaten, RaetselgruppensucheTreffer, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";
import { Observable, tap } from "rxjs";
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
    raetselgruppeBasisdaten$ = this.store.pipe(select(RaetselgruppenSelectors.getRaetselgruppeBasisdaten));


    constructor(private store: Store<fromRaetselgruppen.RaetselgruppenPartialState>, private safeHttpService: SafeHttpService, private httpService: RaetselgruppenHttpService) { }

    public setSuchparameter(suchparameter: RaetselgruppenSuchparameter): void {
        console.log('suchparameter changed');

        this.store.dispatch(RaetselgruppenActions.suchparameterChanged({suchparameter}));
    }

    public selectRaetselgruppe(raetselgruppe: RaetselgruppensucheTrefferItem): void {
        this.store.dispatch(RaetselgruppenActions.selectRaetselgruppe({ raetselgruppe }));
    }

    public createAndEditRaetselgruppe(): void {

        const raetselgruppeBasisdaten: RaetselgruppeBasisdaten = initialRaetselgruppeBasisdaten;
        this.store.dispatch(RaetselgruppenActions.editRaetselgruppe({raetselgruppeBasisdaten}));
    }
}
