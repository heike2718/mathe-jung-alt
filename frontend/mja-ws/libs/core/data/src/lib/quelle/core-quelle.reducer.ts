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
    name: 'coreQuelle',
    reducer: createReducer<CoreQuelleState>(
        initialState,
        on(
            coreQuelleActions.core_quelle_admin_loaded,
            (state, { quelle: quelle }): CoreQuelleState => ({
                ...state,
                quelleAdmin: quelle ? quelle : noopQuelle,
                quelleAdminLoaded: true
            })
        ),
        on(
            coreQuelleActions.core_quelle_admin_remove,
            (_state, _action) => initialState
        )
    )
});
