import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";
import { raetselgruppenFeature, RaetselgruppenEffects} from '@mja-ws/raetselgruppen/data';
 

export const raetselgruppenDataProvider = [
    provideState(raetselgruppenFeature),
    provideEffects([RaetselgruppenEffects])
];
