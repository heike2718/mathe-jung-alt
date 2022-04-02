import { createAction, props } from '@ngrx/store';
import { Stichwort } from '../../entities/stichwort';

export const loadStichwort = createAction('[Stichwort] Load Stichwort');

export const loadStichwortSuccess = createAction(
  '[Stichwort] Load Stichwort Success',
  props<{ stichwort: Stichwort[] }>()
);

export const loadStichwortFailure = createAction(
  '[Stichwort] Load Stichwort Failure',
  props<{ error: any }>()
);
