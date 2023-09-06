import { CoreStatistikEffects, statistikFeature } from "@mja-ws/core/data";
import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";


export const coreStatistikDataProvider = [
    provideState(statistikFeature),
    provideEffects([CoreStatistikEffects])
];
