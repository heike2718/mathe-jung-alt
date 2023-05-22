import { GrafikSearchResult } from "@mja-ws/grafik/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { grafikActions } from "./grafik.actions";


export interface GrafikState {
    readonly loaded: boolean;
    readonly pfad: string | undefined;
    readonly selectedGrafikSearchResult: GrafikSearchResult | undefined;
};

const initialGrafikState: GrafikState = {
    loaded: false,
    pfad: undefined,
    selectedGrafikSearchResult: undefined
};

export const grafikFeature = createFeature({
    name: 'grafik',
    reducer: createReducer(
        initialGrafikState,
        on(grafikActions.pruefe_grafik, (state, _action) => ({ ...state, selectedGrafikSearchResult: undefined })),
        on(grafikActions.grafik_geprueft, (state, action) => ({ ...state, loaded: true, selectedGrafikSearchResult: action.grafikSearchResult })),
        on(grafikActions.grafik_hochgeladen, (state, _action) => ({ ...state, loaded: false, selectedGrafikSearchResult: undefined })),
        on(grafikActions.clear_vorschau, (state, _action) => ({ ...state, loaded: false, selectedGrafikSearchResult: undefined })),
    )
});

