import { state } from '@angular/animations';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { createReducer, on, Action } from '@ngrx/store';
import { initialSuchfilter, Suchfilter } from '../entities/suchfilter';
import * as SuchfilterActions from './suchfilter.actions';


export const SUCHFILTER_FEATURE_KEY = 'suchfilter';

export interface SuchfilterPartialState {
    readonly [SUCHFILTER_FEATURE_KEY]: SuchfilterState;
};

export interface SuchfilterState {
    readonly filter: Suchfilter;
}

const initialState: SuchfilterState = {
    filter: initialSuchfilter
};

const suchfilterReducer = createReducer(
    initialState,

    on(SuchfilterActions.suchkontextChanged, (state, action) => ({
        ...state,
        filter: { ...state.filter, kontext: action.kontext, suchstring: '', deskriptoren: [] },
    })),

    on(SuchfilterActions.suchstringChanged, (state, action) => {

        const neuerFilter = {...state.filter, suchstring: action.suchstring};
        return {...state, filter: neuerFilter};
    }),

    on(SuchfilterActions.deskriptorenChanged, (state, action) => {

        const neuerFilter = {...state.filter, deskriptoren: action.deskriptoren};
        return {...state, filter: neuerFilter};
    }),
);

export function reducer(state: SuchfilterState | undefined, action: Action) {
    return suchfilterReducer(state, action);
}
