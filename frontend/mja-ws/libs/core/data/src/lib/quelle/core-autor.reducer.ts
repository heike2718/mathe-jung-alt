import { initialQuelleDto,  QuelleDto } from "@mja-ws/core/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { coreQuelleActions } from "./core-autor.actions";


export interface CoreQuelleState {
    readonly quelleEigenkreationLoaded: boolean;
    readonly quelleEigenkreation: QuelleDto
};

const initialState: CoreQuelleState = {
    quelleEigenkreationLoaded: false,
    quelleEigenkreation: initialQuelleDto
};

export const coreAutorFeature = createFeature({
    name: 'mjaCoreAutor',
    reducer: createReducer<CoreQuelleState>(
        initialState,
        on(
            coreQuelleActions.cORE_AUTOR_LOADED,
            (state, { quelle: quelle }): CoreQuelleState => ({
                ...state,
                quelleEigenkreation: quelle ? quelle : initialQuelleDto,
                quelleEigenkreationLoaded: true
            })
        ),
        on(
            coreQuelleActions.cORE_AUTOR_REPLACED,
            (state, { quelle: quelle }): CoreQuelleState => ({
                ...state,
                quelleEigenkreation: quelle ? quelle : initialQuelleDto,
                quelleEigenkreationLoaded: true
            })
        ),
        on(
            coreQuelleActions.rEMOVE_AUTOR,
            () => initialState
        )
    )
});
