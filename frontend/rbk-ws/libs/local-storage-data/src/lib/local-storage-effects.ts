import { Injectable } from '@angular/core';
import { fromEvent, map } from 'rxjs';
import { createEffect } from '@ngrx/effects';
import { syncLocalStorage } from './sync-local-storage';
import { filterDefined } from '@rbk-ws/shared/util';

@Injectable()
export class LocalStorageEffects {
  storageEvent = createEffect(() => {
    return fromEvent<StorageEvent>(window, 'storage').pipe(
      map(evt => evt.key),
      // pluck('key'),
      filterDefined,
      map(featureState => syncLocalStorage({ featureState }))
    );
  });
}
