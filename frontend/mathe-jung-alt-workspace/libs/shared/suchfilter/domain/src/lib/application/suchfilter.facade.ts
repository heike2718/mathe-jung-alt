import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import { SuchfilterPartialState } from "../+state/suchfilter.reducer";
import * as SuchfilterActions from '../+state/suchfilter.actions';
import * as SuchfilterSelectors from '../+state/suchfilter.selectors';
import { SuchfilterWithStatus, Suchkontext } from "../entities/suchfilter";
import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { Observable, pipe } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class SuchfilterFacade {

    public suchfilterWithStatus$: Observable<SuchfilterWithStatus> = this.store.pipe(select(SuchfilterSelectors.getSuchfilterAndReady));
    public suchkontext$ = this.store.pipe(select(SuchfilterSelectors.getSuchfilterKontext));
    public suchliste$: Observable<Deskriptor[]> = this.store.pipe(select(SuchfilterSelectors.getSuchliste));
    public restliste$: Observable<Deskriptor[]> = this.store.pipe(select(SuchfilterSelectors.getRestliste));
    public deskriptorenLoaded$: Observable<boolean> = this.store.pipe(select(SuchfilterSelectors.getDeskriptorenLoaded));
    public filteredDeskriptoren$: Observable<Deskriptor[]> = this.store.pipe(select(SuchfilterSelectors.getFilteredDeskriptoren));

    #deskriptorenLoaded = false;

    constructor(private store: Store<SuchfilterPartialState>) {

        this.deskriptorenLoaded$.subscribe(
            loaded => this.#deskriptorenLoaded = loaded
        );
    }

    public loadDeskriptoren(): void {

        if (this.#deskriptorenLoaded) {
            return;
        }

        this.store.dispatch(SuchfilterActions.loadDeskriptoren());
    }

    public clearAll(): void {

        this.store.dispatch(SuchfilterActions.reset());

    }

    public changeSuchkontext(kontext: Suchkontext): void {
        this.store.dispatch(SuchfilterActions.suchkontextChanged({ kontext }));
    }

    public changeSuchtext(suchstring: string): void {
        this.store.dispatch(SuchfilterActions.suchstringChanged({ suchstring }));
    }

    public changeDeskriptoren(deskriptoren: Deskriptor[]): void {
        this.store.dispatch(SuchfilterActions.deskriptorenChanged({ deskriptoren }));
    }

    public addToSearchlist(deskriptor: Deskriptor): void {
        this.store.dispatch(SuchfilterActions.deskriptorAddedToSearchList({ deskriptor }));
    }

    public removeFromSearchlist(deskriptor: Deskriptor): void {
        this.store.dispatch(SuchfilterActions.deskriptorRemovedFromSearchList({ deskriptor }));
    }
}