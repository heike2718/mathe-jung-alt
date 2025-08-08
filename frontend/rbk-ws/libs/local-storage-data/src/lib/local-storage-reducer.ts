import {
  localStorageSync,
  rehydrateApplicationState,
} from 'ngrx-store-localstorage';
import { isSyncLocalStorage } from './is-sync-local-storage';
import { ActionReducer } from '@ngrx/store';


export const localStorageReducer = (...featureStateNames: string[]) => {

  const syncerFn = localStorageSync({
    keys: featureStateNames,
    rehydrate: true,
    restoreDates: false        // kein automatisches Date‑Mapping mehr
  });

  return <S>(reducer: ActionReducer<S>): ActionReducer<S> =>
    (state, action) => {
      if (isSyncLocalStorage(action)) {
        const rehydratedFeatureState = rehydrateApplicationState({
          keys: ['mjaAuth',
            'mjaCoreAutor',
            'mjaCoreDeskriptoren'], //[action.featureState],     // welche Slices
          storage: localStorage,              // Storage-Objekt
          storageKeySerializer: (key: string) => key,      // Identity für den storage‑Key
          restoreDates: false                      // hier restoreDates=false
        });
        return { ...state, ...rehydratedFeatureState };
      }
      return syncerFn(reducer)(state, action);
    };
};
