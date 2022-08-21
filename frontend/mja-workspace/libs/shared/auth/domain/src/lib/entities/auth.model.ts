import { logout } from "../+state/auth.actions";

export const LOGOUT_ACTION_TYPE = logout.type;

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

