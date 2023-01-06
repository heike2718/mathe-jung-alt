import { GrafikSearchResult } from "@mja-ws/grafik/model";
import { Message } from "@mja-ws/shared/messaging/api";
import { createActionGroup, emptyProps, props } from "@ngrx/store";

export const grafikActions = createActionGroup({
    source: 'Grafik',
    events: {
        'PRUEFE_GRAFIK': props<{pfad: string}>(),
        'GRAFIK_GEPRUEFT': props<{grafikSearchResult: GrafikSearchResult}>(),
        'GRAFIK_HOCHGELADEN': props<{message: Message}>(),
        'CLEAR_VORSCHAU': emptyProps()
    }
});

