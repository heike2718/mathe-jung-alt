import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import { SuchfilterPartialState } from "../+state/suchfilter.reducer";
import * as SuchfilterActions from '../+state/suchfilter.actions';
import * as SuchfilterSelectors from '../+state/suchfilter.selectors';
import { Suchfilter } from "../entities/suchfilter";
import { Deskriptor, Suchkontext } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class SuchfilterFacade {

    public suchliste$: Observable<Deskriptor[]> = this.store.pipe(select(SuchfilterSelectors.getSuchliste));
    public restliste$: Observable<Deskriptor[]> = this.store.pipe(select(SuchfilterSelectors.getRestliste));
    public selectedSuchfilter$: Observable<Suchfilter | undefined> = this.store.pipe(select(SuchfilterSelectors.getSelectedSuchfilter));
    public canStartSuche$: Observable<boolean> = this.store.pipe(select(SuchfilterSelectors.isSuchfilterReadyToGo));

    constructor(private store: Store<SuchfilterPartialState>) { }

    public clearAll(): void {

        this.store.dispatch(SuchfilterActions.reset());

    }

    public resetSuchfilter(kontext: Suchkontext): void {
        this.store.dispatch(SuchfilterActions.resetKontext({ kontext }));
    }

    public sucheFinished(kontext: Suchkontext): void {
        this.store.dispatch(SuchfilterActions.markUnchanged({ kontext }));
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
