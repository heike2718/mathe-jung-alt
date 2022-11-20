import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Session } from './internal.model';
import { AuthResult } from '@mja-ws/shared/auth/model';
import { Message } from '@mja-ws/shared/messaging/model';

export const authActions = createActionGroup({
    source: 'Auth',
    events: {
        'Log In': emptyProps(),  // public
        'Request Login Url': emptyProps(), // internal
        'Redirect To Authprovider': props<{ authUrl: string }>(), // internal
        'Create Session': props<{hash: string}>(),
        'Init Session': props<{ authResult: AuthResult }>(), // public
        'Session Created': props<{ session: Session }>(), // internal
        'Session Expired': props<{message: Message}>(), // public?
        'Log Out': emptyProps(), // public
        'Logged Out': emptyProps() // internal
    }
});

