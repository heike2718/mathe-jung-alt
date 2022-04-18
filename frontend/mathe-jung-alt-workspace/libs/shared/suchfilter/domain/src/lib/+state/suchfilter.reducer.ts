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
    readonly suchfilter: Suchfilter;
}

const initialState: SuchfilterState = {
    suchfilter: initialSuchfilter
};

const suchfilterReducer = createReducer(
    initialState,

    on(SuchfilterActions.suchkontextChanged, (state, action) => ({
        ...state,
        suchfilter: { ...state.suchfilter, kontext: action.kontext, suchstring: '', deskriptoren: [] },
    })),

    on(SuchfilterActions.suchstringChanged, (state, action) => ({
        ...state,
        suchfilter: { ...state.suchfilter, suchstring: action.suchstring },
    })),

    on(SuchfilterActions.deskriptorAdded, (state, action) => {

        const deskriptoren = [...state.suchfilter.deskriptoren, action.deskriptor];
        return {
            ...state,
            suchfilter: { ...state.suchfilter, deskriptoren: deskriptoren }
        };
    }),

    on(SuchfilterActions.deskriptorRemoved, (state, action) => {

        const deskriptoren: Deskriptor[] = [];
        const deskriptor: Deskriptor = action.deskriptor;

        state.suchfilter.deskriptoren.forEach(
            d => {
                if (d.id !== deskriptor.id) {
                    deskriptoren.push(d);
                }
            }
        );

        return {
            ...state,
            suchfilter: { ...state.suchfilter, deskriptoren: deskriptoren }
        };
    }),
);

export function reducer(state: SuchfilterState | undefined, action: Action) {
    return suchfilterReducer(state, action);
}
