import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { authFeature, AuthEffects } from '@mja-ws/shared/auth/data';

export const authDataProvider = [
    provideState(authFeature),
    provideEffects(AuthEffects)
];