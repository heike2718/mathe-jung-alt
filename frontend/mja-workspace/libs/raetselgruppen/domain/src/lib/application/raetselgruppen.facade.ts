import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import * as fromRaetselgruppen from '../+state/raetselgruppen.reducer';
import * as RaetselgruppenActions from '../+state/raetselgruppen.actions';
import * as RaetselgruppenSelectors from '../+state/raetselgruppen.selectors';
import { RaetselgruppensucheTreffer, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";
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


    constructor(private store: Store<fromRaetselgruppen.RaetselgruppenPartialState>, private safeHttpService: SafeHttpService, private httpService: RaetselgruppenHttpService) { }

    public setSuchparameter(suchparameter: RaetselgruppenSuchparameter): void {
        console.log('suchparameter changed');

        // const obs$ = this.httpService.findGruppen(suchparameter);
        // const erroMessage = 'Beim Laden der Gruppen ist etwas schiefgegangen';
        // const errorObject: RaetselgruppensucheTreffer = {
        //     anzahlTreffer: 0,
        //     items: []
        // };

        // const treffer = this.#getMock();

        // this.store.dispatch(RaetselgruppenActions.pageLoaded({treffer: treffer}))

        // this.safeHttpService.wrapBackendCall(obs$, erroMessage, errorObject).pipe(
        //     tap((treffer) => this.store.dispatch(RaetselgruppenActions.pageLoaded({treffer})))
        // ).subscribe();

        this.store.dispatch(RaetselgruppenActions.suchparameterChanged({suchparameter}));
    }

    public selectRaetselgruppe(raetselgruppe: RaetselgruppensucheTrefferItem): void {
        this.store.dispatch(RaetselgruppenActions.selectRaetselgruppe({ raetselgruppe }));
    }


    #getMock(): RaetselgruppensucheTreffer {

        const items: RaetselgruppensucheTrefferItem[] = [];

        for (let index = 0; index++; index < 15) {

            const jahr = 2000 + index;

            const item: RaetselgruppensucheTrefferItem = {
                anzahlElemente: 0,
                id: '' + index,
                status: 'ERFASST',
                name: 'Name ' + index,
                referenztyp: 'MINIKAENGURU',
                referenz: '' + jahr
            };

            items.push(item);
        }

        for (let index = 15; index++; index < 20) {

            const item: RaetselgruppensucheTrefferItem = {
                anzahlElemente: 0,
                id: '' + index,
                status: 'ERFASST',
                name: 'Name ' + index,
                referenztyp: 'SERIE',
                referenz: '' + index
            };

            items.push(item);
        }

        console.log('mocked item created');

        return {
            anzahlTreffer: 50,
            items: items
        };
    }
}
