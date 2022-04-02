import { createAction, props } from '@ngrx/store';
import { Medium } from '../../entities/medien';

export const loadMedien = createAction('[Medien] Load Medien');

export const loadMedienSuccess = createAction(
  '[Medien] Load Medien Success',
  props<{ medien: Medium[] }>()
);

export const loadMedienFailure = createAction(
  '[Medien] Load Medien Failure',
  props<{ error: any }>()
);
