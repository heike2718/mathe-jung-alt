import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { MedienEffects, medienFeature } from "@mja-ws/medien/data";
 

export const medienDataProvider = [
    provideState(medienFeature),
    provideEffects(MedienEffects)
];
