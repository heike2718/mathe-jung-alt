import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { RaetselEffects } from "./raetsel.effects";
import { raetselFeature } from './raetsel.reducer';

export const raetselDataProvider = [
    provideState(raetselFeature),
    provideEffects([RaetselEffects])
];
