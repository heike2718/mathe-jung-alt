import { GrafikEffects, grafikFeature } from "@mja-ws/grafik/data";
import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";

export const grafikDataProvider = [
    provideState(grafikFeature),
    provideEffects([GrafikEffects])
];
