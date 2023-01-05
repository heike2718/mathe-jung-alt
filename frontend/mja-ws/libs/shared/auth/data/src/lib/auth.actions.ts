import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Session } from './internal.model';
import { AuthResult } from '@mja-ws/shared/auth/model';

export const authActions = createActionGroup({
    source: 'Auth',
    events: {
        'LOG_IN': emptyProps(),  // internal
        'REQUEST_LOGIN_URL': emptyProps(), // internal
        'REDIRECT_TO_AUTH': props<{ authUrl: string }>(), // internal
        'CREATE_SESSION': props<{hash: string}>(),
        'INIT_SESSION': props<{ authResult: AuthResult }>(), // public
        'SESSION_CREATED': props<{ session: Session }>(), // internal
        'LOG_OUT': emptyProps(), // public
        'LOGGED_OUT': emptyProps() // public
    }
    
});

