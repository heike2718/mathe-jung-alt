import { createReducer, on, Action } from '@ngrx/store';
import * as RaetselgruppenActions from './raetselgruppen.actions';

export const RAETSELGRUPPEN_FEATURE_KEY = 'raetselgruppen';

import { initialRaetselgruppenSuchparameter, RaetselgruppeBasisdaten, RaetselgruppeDetails, Raetselgruppenelement, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";

export interface RaetselgruppenState {
    readonly page: RaetselgruppensucheTrefferItem[];
    readonly anzahlTrefferGesamt: number;
    readonly suchparameter: RaetselgruppenSuchparameter,
    readonly raetselgruppeBasisdaten: RaetselgruppeBasisdaten | undefined;
    readonly raetselgruppeDetails: RaetselgruppeDetails | undefined;
};

export interface RaetselgruppenPartialState {
    readonly [RAETSELGRUPPEN_FEATURE_KEY]: RaetselgruppenState;
};

const initialState: RaetselgruppenState = {
    page: [],
    anzahlTrefferGesamt: 0,
    suchparameter: initialRaetselgruppenSuchparameter,
    raetselgruppeBasisdaten: undefined,
    raetselgruppeDetails: undefined
};

const raetselgruppenReducer = createReducer(
    initialState,

    on(RaetselgruppenActions.suchparameterChanged, (state, action) => {
        return {
            ...state,
            suchparameter: action.suchparameter
        };
    }),

    on(RaetselgruppenActions.pageLoaded, (state, action) => {
        return {
            ...state,
            page: action.treffer.items,
            anzahlTrefferGesamt: action.treffer.anzahlTreffer
        };
    }),

    on(RaetselgruppenActions.editRaetselgruppe, (state, action) => {

        // console.log(action.raetselgruppeBasisdaten);
        return { ...state, raetselgruppeBasisdaten: action.raetselgruppeBasisdaten };
    }),

    on(RaetselgruppenActions.raetselgruppeSaved, (state, action) => {
        const trefferItem: RaetselgruppensucheTrefferItem = action.raetselgruppe;
        return { ...state, raetselgruppeBasisdaten: trefferItem };
    }),

    on(RaetselgruppenActions.raetselgruppeDetailsLoaded, (state, action) => ({ ...state, raetselgruppeDetails: action.raetraetselgruppeDetails })),

    on(RaetselgruppenActions.raetselgruppenelementeChanged, (state, action) => ({ ...state, raetselgruppeDetails: action.raetraetselgruppeDetails })),

    on(RaetselgruppenActions.unselectRaetselgruppe, (state, _action) => ({ ...state, raetselgruppeDetails: undefined, raetselgruppeBasisdaten: undefined })),

);

export function reducer(state: RaetselgruppenState | undefined, action: Action) {
    return raetselgruppenReducer(state, action);
};
