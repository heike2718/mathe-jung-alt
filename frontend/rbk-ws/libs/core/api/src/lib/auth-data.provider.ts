import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { authFeature, AuthEffects } from '@rbk-ws/core/data';

export const authDataProvider = [provideState(authFeature), provideEffects(AuthEffects)];
