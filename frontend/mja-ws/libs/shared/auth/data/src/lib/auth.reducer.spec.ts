import "@angular/compiler";  // erforderlich weil man aktuell eine bescheuerte Fehlermeldung von jest bekommt, obwohl alles korrekt konfigiriert ist
import { authFeature, initialState } from './auth.reducer';
import { authActions } from './auth.actions';
import { anonymousSession, Session } from './internal.model';

describe('Auth Reducer', () => {

    it('should replace the session in state when session created', () => {

        const theNewSession: Session = {
            expiresAt: 99999,
            user: {
                anonym: false,
                fullName: 'Ponder Stibbons',
                idReference: 'hjkahkj',
                roles: ['ADMIN', 'STANDARD']
            }
        }
        const state = initialState;
        const newState = authFeature.reducer(state, authActions.sessionCreated({ session: theNewSession }));
        expect(newState.session).toBe(theNewSession);
    });

    it('should reset the session when logged out', () => {

        const theSession: Session = {
            expiresAt: 99999,
            user: {
                anonym: false,
                fullName: 'Ponder Stibbons',
                idReference: 'hjkahkj',
                roles: ['ADMIN', 'STANDARD']
            }
        }
        const state = {...initialState, session: theSession};
        const newState = authFeature.reducer(state, authActions.loggedOut());
        expect(newState.session).toBe(anonymousSession);
    });
});