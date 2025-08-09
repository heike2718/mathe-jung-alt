import {
  localStorageSync,
  rehydrateApplicationState,
} from 'ngrx-store-localstorage';
import { isSyncLocalStorage } from './is-sync-local-storage';
import { ActionReducer } from '@ngrx/store';

const LOCAL_STORAGE_KEYS = ['mjaAuth',
            'mjaCoreAutor',
            'mjaCoreDeskriptoren'];

export const localStorageReducer = () => {

  const syncerFn = localStorageSync({
    keys: LOCAL_STORAGE_KEYS,
    rehydrate: true,
    restoreDates: false        // kein automatisches Date‑Mapping mehr
  });

  return <S>(reducer: ActionReducer<S>): ActionReducer<S> =>
    (state, action) => {
      if (isSyncLocalStorage(action)) {
        const rehydratedFeatureState = rehydrateApplicationState({
          keys: LOCAL_STORAGE_KEYS, //[action.featureState],     // welche Slices
          storage: localStorage,              // Storage-Objekt
          storageKeySerializer: (key: string) => key,      // Identity für den storage‑Key
          restoreDates: false                      // hier restoreDates=false
        });
        return { ...state, ...rehydratedFeatureState };
      }
      return syncerFn(reducer)(state, action);
    };
};
