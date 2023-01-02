import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { CoreDeskriptorUIEffects } from "./core-deskriptoren.effects";
import { coreDeskriptorenUIFeature } from "./core-deskriptoren.reducer";

export const coreDeskriptorenDataProvider = [
    provideState(coreDeskriptorenUIFeature),
    provideEffects([CoreDeskriptorUIEffects])
];
