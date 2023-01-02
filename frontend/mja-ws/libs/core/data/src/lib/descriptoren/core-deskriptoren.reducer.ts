import { DeskriptorUI } from "@mja-ws/core/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { coreDeskriptorenActions } from "./core-deskriptoren.actions";

export interface CoreDeskriptorenUIState {
    readonly deskriptorenUILoaded: boolean;
    readonly deskriptoren: DeskriptorUI[];
}

const initialState: CoreDeskriptorenUIState = {
    deskriptorenUILoaded: false,
    deskriptoren: []
};

export const coreDeskriptorenUIFeature = createFeature({
    name: 'coreDeskriptoren',
    reducer: createReducer<CoreDeskriptorenUIState>(
        initialState,
        on(
            coreDeskriptorenActions.core_deskriptoren_loaded,
            (state, { deskriptoren: deskriptoren }): CoreDeskriptorenUIState => (
                {
                    ...state,
                    deskriptorenUILoaded: true,
                    deskriptoren: deskriptoren
                }
            )
        ),
        on(
            coreDeskriptorenActions.core_deskriptoren_remove,
            (state_, action_) => initialState
        )
    )
});
