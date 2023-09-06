import { AnzahlabfrageErgebnis } from "@mja-ws/core/model";
import { createActionGroup, emptyProps, props } from "@ngrx/store";

export const coreStatistikActions = createActionGroup({
    source: 'CoreStatistik',
    events: {
        'LOAD_ANZAHL_RAETSEL_PUBLIC': emptyProps(),
        'ANZAHL_RAETSEL_PUBLIC_LOADED': props<{payload: AnzahlabfrageErgebnis}>()
    }
});