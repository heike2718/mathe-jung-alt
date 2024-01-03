import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { AuthResult, Session } from '@mja-ws/core/model';

export const authActions = createActionGroup({
    source: 'Auth',
    events: {
        'LOG_IN': emptyProps(),
        'REQUEST_LOGIN_URL': emptyProps(),
        'REQUEST_SIGNUP_URL': emptyProps(), 
        'REDIRECT_TO_AUTH': props<{ authUrl: string }>(), 
        'CREATE_SESSION': props<{hash: string}>(),
        'INIT_SESSION': props<{ authResult: AuthResult }>(), 
        'SESSION_CREATED': props<{ session: Session }>(),
        'LOG_OUT': emptyProps(),
        'LOGGED_OUT': emptyProps()
    }
    
});

