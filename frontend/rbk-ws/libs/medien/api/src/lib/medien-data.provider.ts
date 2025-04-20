import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { MedienEffects, medienFeature } from "@rbk-ws/medien/data";
 

export const medienDataProvider = [
    provideState(medienFeature),
    provideEffects(MedienEffects)
];
