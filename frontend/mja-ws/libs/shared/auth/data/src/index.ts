export { AuthRepository } from './lib/auth.repository';
export { authFeature } from './lib/auth.reducer';
export { AuthEffects } from './lib/auth.effects';
import { authActions as allAuthActions } from './lib/auth.actions';

// publish logged_out-Action
const { logged_out } = allAuthActions;

export const authActions = { logged_out };