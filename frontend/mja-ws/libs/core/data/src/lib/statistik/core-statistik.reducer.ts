import { createFeature, createReducer, on } from "@ngrx/store";
import { coreStatistikActions } from "./core-statistik.actions";


export interface StatistikState {
    readonly anzahlPublicRaetselLoaded: boolean;
    readonly anzahlPublicRaetsel: number;
};

const initialState: StatistikState = {
    anzahlPublicRaetselLoaded: false,
    anzahlPublicRaetsel: 0
};

export const statistikFeature = createFeature({
    name: 'Statistik',
    reducer: createReducer(
        initialState,
        on(coreStatistikActions.aNZAHL_RAETSEL_PUBLIC_LOADED, (state, action) => {
            return {
                ...state,
                anzahlPublicRaetsel: action.payload.ergebnis,
                anzahlPublicRaetselLoaded: true
            }
        })        
    )
});