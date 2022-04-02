import { createAction, props } from '@ngrx/store';
import { Quelle } from '../../entities/quelle';

export const loadQuelle = createAction('[Quelle] Load Quelle');

export const loadQuelleSuccess = createAction(
  '[Quelle] Load Quelle Success',
  props<{ quelle: Quelle[] }>()
);

export const loadQuelleFailure = createAction(
  '[Quelle] Load Quelle Failure',
  props<{ error: any }>()
);
