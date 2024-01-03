export type Benutzerart = 'ANONYM' | 'ADMIN' | 'AUTOR' | 'STANDARD';

export interface AuthResult {
  expiresAt: number | undefined;
  state: string | undefined;
  nonce: string | undefined;
  idToken: string | undefined;
};

export interface User {
  readonly fullName: string,
  readonly benutzerart: Benutzerart,
  readonly isAdmin: boolean,
  readonly anonym: boolean
};

export interface UserFull {
  readonly idReference: string;
  readonly roles: string[];
  readonly fullName: string;
  readonly benutzerart: Benutzerart;
  readonly anonym: boolean;
};

export interface Session {
  readonly sessionId: string | undefined;
  readonly expiresAt: number;
  readonly user: UserFull;
};

const anonymousUser: UserFull = {
  fullName: 'Gast',
  idReference: 'ANONYM',
  roles: [],
  benutzerart: 'ANONYM',
  anonym: true
};

export const anonymousSession: Session = {
  sessionId: undefined,
  expiresAt: 0,
  user: anonymousUser
};

export function isAdmin(user: UserFull | undefined): boolean {

  if (user === undefined) {
    return false;
  }

  return user.benutzerart === 'ADMIN' || user.benutzerart === 'AUTOR';
};

export function isAnonymousSession(session: Session): boolean {

  if (!session) {
    return false;
  }
  if (!session.user) {
    return false;
  }

  return session.user.roles.length === 0;
}
