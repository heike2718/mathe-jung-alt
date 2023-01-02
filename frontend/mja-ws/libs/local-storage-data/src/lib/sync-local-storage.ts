import { createAction, props } from '@ngrx/store';

export const syncLocalStorage = createAction(
  '[Core] Sync Local Store',
  props<{ featureState: string }>()
);
