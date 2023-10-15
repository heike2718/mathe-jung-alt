import { noopQuelle, QuelleUI } from "@mja-ws/core/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { coreQuelleActions } from "./core-quelle.actions";


export interface CoreQuelleState {
    readonly quelleAdminLoaded: boolean;
    readonly quelleAdmin: QuelleUI
};

const initialState: CoreQuelleState = {
    quelleAdminLoaded: false,
    quelleAdmin: noopQuelle
};

export const coreQuelleFeature = createFeature({
    name: 'mjaCoreQuelle',
    reducer: createReducer<CoreQuelleState>(
        initialState,
        on(
            coreQuelleActions.cORE_QUELLE_ADMIN_LOADED,
            (state, { quelle: quelle }): CoreQuelleState => ({
                ...state,
                quelleAdmin: quelle ? quelle : noopQuelle,
                quelleAdminLoaded: true
            })
        ),
        on(
            coreQuelleActions.cORE_QUELLE_ADMIN_REMOVE,
            (_state, _action) => {
                localStorage.setItem('mjaCoreQuelle', JSON.stringify(initialState));
                return initialState;
            }
        )
    )
});
