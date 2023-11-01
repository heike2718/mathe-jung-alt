import { createFeature, createReducer, on } from "@ngrx/store";
import { grafikActions } from "./grafik.actions";
import { EmbeddableImageVorschau } from "@mja-ws/embeddable-images/model";
import { Message } from "@mja-ws/shared/messaging/api";


export interface GrafikState {
    readonly loaded: boolean;
    readonly pfad: string | undefined;
    readonly selectedEmbeddableImageVorschau: EmbeddableImageVorschau | undefined;
    readonly grafikHochgeladenMessage: Message | undefined
};

const initialGrafikState: GrafikState = {
    loaded: false,
    pfad: undefined,
    selectedEmbeddableImageVorschau: undefined,
    grafikHochgeladenMessage: undefined
};

export const grafikFeature = createFeature({
    name: 'grafik',
    reducer: createReducer(
        initialGrafikState,
        on(grafikActions.lADE_VORSCHAU, (state, _action) => ({ ...state, selectedEmbeddableImageVorschau: undefined })),
        on(grafikActions.vORSCHAU_GELADEN, (state, action) => ({ ...state, loaded: true, selectedEmbeddableImageVorschau: action.embeddableImageVorschau, grafikHochgeladenMessage: undefined })),
        on(grafikActions.gRAFIK_HOCHGELADEN, (state, action) => ({ ...state, loaded: false, grafikHochgeladenMessage: action.message })),
        on(grafikActions.cLEAR_VORSCHAU, (state, _action) => ({ ...state, loaded: false, selectedEmbeddableImageVorschau: undefined })),
    )
});

