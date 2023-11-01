import { EmbeddableImageVorschau } from "@mja-ws/embeddable-images/model";
import { Message } from "@mja-ws/shared/messaging/api";
import { createActionGroup, emptyProps, props } from "@ngrx/store";

export const grafikActions = createActionGroup({
    source: 'Grafik',
    events: {
        'LADE_VORSCHAU': props<{pfad: string}>(),
        'VORSCHAU_GELADEN': props<{embeddableImageVorschau: EmbeddableImageVorschau}>(),
        'GRAFIK_HOCHGELADEN': props<{message: Message}>(),
        'CLEAR_VORSCHAU': emptyProps()
    }
});

