import { createAction, props } from '@ngrx/store';
import { AuthResult, Session } from '../entities/auth.model';

export const requestLoginUrl = createAction(
    '[AuthFacade] request login url'
);

export const getRedirectLoginUrlSuccess = createAction(
    '[Auth] redirect login url success',
    props<{ authUrl: string }>()
);

export const createSession = createAction(
    '[Auth] createSession',
    props<{ authResult: AuthResult }>()
);

export const sessionCreated = createAction(
    '[Auth] session created',
    props<{ session: Session }>()
);

export const userLoggedIn = createAction(
    '[Auth] logged in',
    props<{ session: Session }>()
);

export const clearOrRestoreSession = createAction(
    '[Auth] clear/restore session'
);

export const sessionCleared = createAction(
    '[Auth] session cleared'
);

export const sessionRestored = createAction(
    '[Auth] session restored',
    props<{ session: Session }>()
);


export const logout = createAction(
    '[Auth] log out'
);

export const userLoggedOut = createAction(
    '[Auth] logged out'
);
