import { createReducer, on, Action } from '@ngrx/store';
import * as RaetselgruppenActions from './raetselgruppen.actions';

export const RAETSELGRUPPEN_FEATURE_KEY = 'raetselgruppen';

import { initialRaetselgruppenSuchparameter, RaetselgruppeBasisdaten, RaetselgruppeDetails, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";


export interface RaetselgruppenState {
    readonly page: RaetselgruppensucheTrefferItem[];
    readonly anzahlTrefferGesamt: number;
    readonly suchparameter: RaetselgruppenSuchparameter,
    readonly selectedGruppe: RaetselgruppensucheTrefferItem | undefined;
    readonly raetselgruppeBasisdaten: RaetselgruppeBasisdaten | undefined;
};

export interface RaetselgruppenPartialState {
    readonly [RAETSELGRUPPEN_FEATURE_KEY]: RaetselgruppenState;
};

const initialState: RaetselgruppenState = {
    page: [],
    anzahlTrefferGesamt: 0,
    suchparameter: initialRaetselgruppenSuchparameter,
    selectedGruppe: undefined,
    raetselgruppeBasisdaten: undefined
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
        return { ...state, raetselgruppeBasisdaten: trefferItem, selectedGruppe: trefferItem};
    })
);

export function reducer(state: RaetselgruppenState | undefined, action: Action) {
    return raetselgruppenReducer(state, action);
};
