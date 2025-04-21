import { ActionReducer } from '@ngrx/store';
import {
  localStorageSync,
  rehydrateApplicationState,
} from 'ngrx-store-localstorage';
import { isSyncLocalStorage } from './is-sync-local-storage';

export const localStorageReducer = (...featureStateNames: string[]) => {
  
  const syncerFn = localStorageSync({
    keys: featureStateNames,
    rehydrate: true,
    restoreDates: false        // ← kein automatisches Date‑Mapping mehr
  });

  return <S>(reducer: ActionReducer<S>): ActionReducer<S> =>
    (state, action) => {
      if (isSyncLocalStorage(action)) {
        const rehydratedFeatureState = rehydrateApplicationState(
          [action.featureState],     // welche Slices
          localStorage,              // Storage-Objekt
          (key: string) => key,      // Identity für den storage‑Key
          false                      // hier restoreDates=false
        );
        return { ...state, ...rehydratedFeatureState };
      }
      return syncerFn(reducer)(state, action);
    };
};
