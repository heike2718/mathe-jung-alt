import { coreDeskriptorenUIFeature, CoreDeskriptorUIEffects } from "@rbk-ws/core/data";
import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";

export const coreDeskriptorenDataProvider = [
    provideState(coreDeskriptorenUIFeature),
    provideEffects(CoreDeskriptorUIEffects)
];
