import { initialHerkunftRaetsel,  HerkunftRaetsel } from "@mja-ws/core/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { coreQuelleActions } from "./core-autor.actions";


export interface CoreQuelleState {
    readonly herkunftEigenkreationLoaded: boolean;
    readonly herkunftEigenkreation: HerkunftRaetsel
};

const initialState: CoreQuelleState = {
    herkunftEigenkreationLoaded: false,
    herkunftEigenkreation: initialHerkunftRaetsel
};

export const coreAutorFeature = createFeature({
    name: 'mjaCoreAutor',
    reducer: createReducer<CoreQuelleState>(
        initialState,
        on(
            coreQuelleActions.cORE_AUTOR_LOADED,
            (state, { quelle: quelle }): CoreQuelleState => ({
                ...state,
                herkunftEigenkreation: quelle ? quelle : initialHerkunftRaetsel,
                herkunftEigenkreationLoaded: true
            })
        ),
        on(
            coreQuelleActions.cORE_AUTOR_REPLACED,
            (state, { quelle: quelle }): CoreQuelleState => ({
                ...state,
                herkunftEigenkreation: quelle ? quelle : initialHerkunftRaetsel,
                herkunftEigenkreationLoaded: true
            })
        ),
        on(
            coreQuelleActions.rEMOVE_AUTOR,
            () => initialState
        )
    )
});
