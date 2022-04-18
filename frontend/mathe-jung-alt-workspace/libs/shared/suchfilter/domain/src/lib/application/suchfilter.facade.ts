import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import { SuchfilterPartialState } from "../+state/suchfilter.reducer";
import * as SuchfilterActions from '../+state/suchfilter.actions';
import * as SuchfilterSelectors from '../+state/suchfilter.selectors';
import { SUCHKONTEXT } from "../entities/suchfilter";
import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";


@Injectable({
    providedIn: 'root'
})
export class SuchfilterFacade {

    public suchfilter$ = this.store.pipe(select(SuchfilterSelectors.getSuchfilter));
    public isSuchfilterReadyToGo$ = this.store.pipe(select(SuchfilterSelectors.isSuchfilterReadyToGo));

    constructor(private store: Store<SuchfilterPartialState>) { }

    public changeSuchkontext(kontext: SUCHKONTEXT): void {
        this.store.dispatch(SuchfilterActions.suchkontextChanged({kontext}));
    }

    public changeSuchtext(suchstring: string): void {
        this.store.dispatch(SuchfilterActions.suchstringChanged({suchstring}));
    }

    public addDeskriptor(deskriptor: Deskriptor): void {
        this.store.dispatch(SuchfilterActions.deskriptorAdded({deskriptor}));
    }

    public removeDeskriptor(deskriptor: Deskriptor): void {
        this.store.dispatch(SuchfilterActions.deskriptorRemoved({deskriptor}));
    }
}