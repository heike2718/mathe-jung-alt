import { Store } from '@ngrx/store';
import {authFeature, initialState} from './auth.reducer';
import { authActions } from './auth.actions';
import { Session } from './internal.model';

describe('Auth Reducer', () => {

    it ('foo', () => {

        const session: Session = {
            expiresAt: 99999,
            user: {
                anonym: false,
                fullName: 'Ponder Stibbons',
                idReference: 'hjkahkj',
                roles: ['ADMIN', 'STANDARD']
            }
        }
        const state = initialState;
        const newState = authFeature.reducer(state, authActions.sessionCreated({session: session}));
        expect(state).toBe(newState);

    });
});