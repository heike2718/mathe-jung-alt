import { RaetselEffects, raetselFeature } from "@rbk-ws/raetsel/data";
import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";

export const raetselDataProvider = [
    provideState(raetselFeature),
    provideEffects(RaetselEffects)
];
