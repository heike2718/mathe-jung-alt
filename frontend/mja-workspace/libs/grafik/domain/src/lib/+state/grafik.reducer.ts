import { createReducer, on, Action } from '@ngrx/store';
import { GrafikSearchResult, nullGraphicSearchResult } from '../entities/grafik.model';
import * as GrafikActions from './grafik.actions';

export const GRAFIK_FEATURE_KEY = 'grafik';

export interface GrafikState {
    readonly loading: boolean;
    readonly loaded: boolean;
    readonly pfad?: string;
    readonly selectedGrafikSearchResult: GrafikSearchResult;
};

export const initialGrafikState: GrafikState = {
    loading: false,
    loaded: false,
    selectedGrafikSearchResult: nullGraphicSearchResult
};

const grafikReducer = createReducer(
    initialGrafikState,

    on(GrafikActions.pruefeGrafik, (state, _action) => ({ ...state, loading: true, selectedGrafikSearchResult:  nullGraphicSearchResult})),
    on(GrafikActions.grafikGeprueft, (state, action) => ({ ...state, loading: false, loaded: true, selectedGrafikSearchResult: action.grafikSearchResult }))
);


export function reducer(state: GrafikState | undefined, action: Action) {
    return grafikReducer(state, action);
};
