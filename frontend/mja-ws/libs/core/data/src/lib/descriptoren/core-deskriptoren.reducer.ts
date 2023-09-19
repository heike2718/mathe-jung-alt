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
            coreDeskriptorenActions.cORE_DESKRIPTOREN_LOADED,
            (state, { deskriptoren: deskriptoren }): CoreDeskriptorenUIState => (
                {
                    ...state,
                    deskriptorenUILoaded: true,
                    deskriptoren: deskriptoren
                }
            )
        ),
        on(
            coreDeskriptorenActions.cORE_DESKRIPTOREN_REMOVE,
            (state_, action_) => initialState
        )
    )
});
