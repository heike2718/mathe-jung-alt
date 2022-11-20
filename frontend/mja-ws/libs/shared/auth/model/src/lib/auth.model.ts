export interface AuthResult {
  expiresAt?: number;
  state?: string;
  nonce?: string;
  idToken?: string;
};

export interface User {
  readonly fullName: string,
  readonly isAdmin: boolean
};
