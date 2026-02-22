import { CoreAutorEffects, coreAutorFeature } from '@rbk-ws/core/data';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

export const coreQuelleDataProvider = [provideState(coreAutorFeature), provideEffects(CoreAutorEffects)];
