import { createAction, props } from '@ngrx/store';
import { Bild } from '../../entities/bild';

export const loadBild = createAction('[Bild] Load Bild');

export const loadBildSuccess = createAction(
  '[Bild] Load Bild Success',
  props<{ bild: Bild[] }>()
);

export const loadBildFailure = createAction(
  '[Bild] Load Bild Failure',
  props<{ error: any }>()
);
