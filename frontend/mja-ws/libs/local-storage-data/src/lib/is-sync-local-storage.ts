import { Action } from '@ngrx/store';
import { syncLocalStorage } from './sync-local-storage';

export function isSyncLocalStorage(
  action: Action
): action is ReturnType<typeof syncLocalStorage> {
  return action.type === syncLocalStorage.type;
}
