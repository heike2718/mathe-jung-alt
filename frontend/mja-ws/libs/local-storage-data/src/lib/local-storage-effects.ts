import { Injectable } from '@angular/core';
import { fromEvent, map } from 'rxjs';
import { createEffect } from '@ngrx/effects';
import { syncLocalStorage } from './sync-local-storage';
import { filterDefined } from '@mja-ws/shared/util';

@Injectable()
export class LocalStorageEffects {
  storageEvent = createEffect(() => {
    return fromEvent<StorageEvent>(window, 'storage').pipe(
      map(x => x?'key':''),
      // pluck('key'),
      filterDefined,
      map((featureState) => syncLocalStorage({ featureState }))
    );
  });
}
