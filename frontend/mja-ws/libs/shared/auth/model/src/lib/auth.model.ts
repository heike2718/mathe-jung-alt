export interface AuthResult {
  expiresAt: number | undefined;
  state: string | undefined;
  nonce: string | undefined;
  idToken: string | undefined;
};

export interface User {
  readonly fullName: string,
  readonly isAdmin: boolean,
  readonly anonym: boolean
};
