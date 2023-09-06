export type BENUTZERART = 'ANONYM' | 'ADMIN' | 'AUTOR' | 'STANDARD';

export interface AuthResult {
  expiresAt: number | undefined;
  state: string | undefined;
  nonce: string | undefined;
  idToken: string | undefined;
};

export interface User {
  readonly fullName: string,
  readonly benutzerart: BENUTZERART,
  readonly isAdmin: boolean,
  readonly anonym: boolean
};
