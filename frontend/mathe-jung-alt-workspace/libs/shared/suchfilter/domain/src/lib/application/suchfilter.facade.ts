import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import { SuchfilterPartialState } from "../+state/suchfilter.reducer";
import * as SuchfilterActions from '../+state/suchfilter.actions';
import * as SuchfilterSelectors from '../+state/suchfilter.selectors';
import { Suchfilter, SuchfilterWithStatus, Suchkontext } from "../entities/suchfilter";
import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { Observable, pipe } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class SuchfilterFacade {

    public allDeskriptoren$: Observable<Deskriptor[]> = this.store.pipe(select(SuchfilterSelectors.getAllDeskriptoren));
    public suchliste$: Observable<Deskriptor[]> = this.store.pipe(select(SuchfilterSelectors.getSuchliste));
    public restliste$: Observable<Deskriptor[]> = this.store.pipe(select(SuchfilterSelectors.getRestliste));
    public deskriptorenLoaded$: Observable<boolean> = this.store.pipe(select(SuchfilterSelectors.getDeskriptorenLoaded));
    public selectedSuchfilter$: Observable<Suchfilter | undefined> = this.store.pipe(select(SuchfilterSelectors.getSelectedSuchfilter));
    public canStartSuche$: Observable<boolean> = this.store.pipe(select(SuchfilterSelectors.isSuchfilterReadyToGo));

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

    // PENDING
    public resetSuchfilter(kontext: Suchkontext): void {

    }

    public sucheFinished(kontext: Suchkontext): void {
        this.store.dispatch(SuchfilterActions.markUnchanged({kontext}));
    }

    public changeSuchkontext(kontext: Suchkontext): void {
        this.store.dispatch(SuchfilterActions.suchkontextChanged({ kontext }));
    }

    public changeSuchtext(suchstring: string): void {
        this.store.dispatch(SuchfilterActions.suchstringChanged({ suchstring }));
    }

    public addToSearchlist(deskriptor: Deskriptor): void {
        this.store.dispatch(SuchfilterActions.deskriptorAddedToSearchList({ deskriptor }));
    }

    public removeFromSearchlist(deskriptor: Deskriptor): void {
        this.store.dispatch(SuchfilterActions.deskriptorRemovedFromSearchList({ deskriptor }));
    }
}
