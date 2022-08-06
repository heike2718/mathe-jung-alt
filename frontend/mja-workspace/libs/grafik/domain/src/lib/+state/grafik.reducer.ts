import { createReducer, on, Action } from '@ngrx/store';
import { GrafikSearchResult } from '../entities/grafik.model';
import * as GrafikActions from './grafik.actions';

export const GRAFIK_FEATURE_KEY = 'grafik';

export interface GrafikState {
    readonly loading: boolean;
    readonly loaded: boolean;
    readonly pfad?: string;
    readonly grafikSearchResult: GrafikSearchResult
};

export const initialGrafikState: GrafikState = {
    loading: false,
    loaded: false,
    grafikSearchResult: {pfad: '', messagePayload: {level: 'INFO', message: ''}}
};

const grafikReducer = createReducer(
    initialGrafikState,

    on(GrafikActions.pruefeGrafik, (state, _action) => ({ ...state, loading: true })),
    on(GrafikActions.grafikGeprueft, (state, action) => ({ ...state, loading: false, loaded: true, grafikSearchResult: action.grafikSearchResult }))
);


export function reducer(state: GrafikState | undefined, action: Action) {
    return grafikReducer(state, action);
};
