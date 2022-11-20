export interface UserFull {
  readonly idReference: string;
  readonly roles: string[];
  readonly fullName: string;
  readonly anonym: boolean;
};

export interface Session {
  readonly sessionId?: string;
  readonly expiresAt: number;
  readonly user: UserFull
};

const anonymousUser: UserFull = {
  fullName: 'Gast',
  idReference: 'ANONYM',
  roles: [],
  anonym: true
};

export const anonymousSession: Session = {
  expiresAt: 0,
  user: anonymousUser
};

export function findRole(role: string, roles: string[]): boolean {

  return roles.filter(r => r === role).length > 0;

};

export function isAdmin(user?: UserFull): boolean {

  if (user === undefined) {
    return false;
  }

  return findRole('ADMIN', user.roles) || findRole('AUTOR', user.roles);
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
