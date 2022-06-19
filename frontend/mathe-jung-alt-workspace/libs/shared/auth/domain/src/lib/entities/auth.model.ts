export const STORAGE_KEY_SESSION_EXPIRES_AT = 'session_expires_at';
export const STORAGE_KEY_DEV_SESSION_ID = 'dev_session_id';
export const STORAGE_KEY_USER = 'user';
export const STORAGE_KEY_INVALID_SESSION = 'sessionInvalidated';

export interface AuthResult {
    expiresAt?: number;
    state?: string;
    nonce?: string;
    idToken?: string;
  };
  
  export interface User {
    readonly idReference: string;
    readonly roles: string[];
    readonly fullName: string
  };
  
  export interface Session {
    readonly sessionId?: string;
    readonly expiresAt: number;
    readonly user: User
  };
  
export function findRole(role: string, roles: string[]): boolean {

  return roles.filter(r => r === role).length > 0;

};

export function isAdmin(user?: User): boolean {

  if (user === undefined) {
    return false;
  }

  return findRole('ADMIN', user.roles);
};

