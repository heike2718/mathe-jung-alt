import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { CoreQuelleEffects } from "./core-quelle.effects";
import { coreQuelleFeature } from "./core-quelle.reducer";


export const coreQuelleDataProvider = [
    provideState(coreQuelleFeature),
    provideEffects([CoreQuelleEffects])
];
