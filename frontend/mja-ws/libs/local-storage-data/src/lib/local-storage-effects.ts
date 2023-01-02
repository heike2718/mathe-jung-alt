import { Injectable } from '@angular/core';
import { fromEvent, map, pluck } from 'rxjs';
import { createEffect } from '@ngrx/effects';
import { syncLocalStorage } from './sync-local-storage';
import { filterDefined } from '@mja-ws/shared/ngrx-utils';

@Injectable()
export class LocalStorageEffects {
  storageEvent = createEffect(() => {
    return fromEvent<StorageEvent>(window, 'storage').pipe(
      pluck('key'),
      filterDefined,
      map((featureState) => syncLocalStorage({ featureState }))
    );
  });
}
