import { RaetselEffects, raetselFeature } from "@mja-ws/raetsel/data";
import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";

export const raetselDataProvider = [
    provideState(raetselFeature),
    provideEffects(RaetselEffects)
];
