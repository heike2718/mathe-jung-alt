import { Injectable } from "@angular/core";
import { select, Store } from "@ngrx/store";
import { SuchfilterPartialState } from "../+state/suchfilter.reducer";
import * as SuchfilterActions from '../+state/suchfilter.actions';
import * as SuchfilterSelectors from '../+state/suchfilter.selectors';
import { SuchfilterWithStatus, Suchkontext } from "../entities/suchfilter";
import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class SuchfilterFacade {

    public suchfilterWithStatus$: Observable<SuchfilterWithStatus> = this.store.pipe(select(SuchfilterSelectors.getSuchfilterAndReady));

    constructor(private store: Store<SuchfilterPartialState>) { }

    public changeSuchkontext(kontext: Suchkontext): void {
        this.store.dispatch(SuchfilterActions.suchkontextChanged({kontext}));
    }

    public changeSuchtext(suchstring: string): void {
        this.store.dispatch(SuchfilterActions.suchstringChanged({suchstring}));
    }

    public changeDeskriptoren(deskriptoren: Deskriptor[]): void {
        this.store.dispatch(SuchfilterActions.deskriptorenChanged({deskriptoren}));
    }
}