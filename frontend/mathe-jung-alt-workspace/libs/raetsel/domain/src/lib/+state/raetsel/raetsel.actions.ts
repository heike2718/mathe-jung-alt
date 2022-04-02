import { createAction, props } from '@ngrx/store';
import { RaetselDetails, Raetsel } from '../../entities/raetsel';

export const loadRaetsel = createAction('[Raetsel] Load Raetsel');

export const loadRaetselSuccess = createAction(
  '[Raetsel] Load Raetsel Success',
  props<{ raetsel: Raetsel[] }>()
);

export const loadRaetselFailure = createAction(
  '[Raetsel] Load Raetsel Failure',
  props<{ error: any }>()
);
