import { createAction, props } from '@ngrx/store';
import { Deskriptor } from '../../entities/deskriptor';

export const loadDeskriptor = createAction('[Deskriptor] Load Deskriptor');

export const loadDeskriptorSuccess = createAction(
  '[Deskriptor] Load Deskriptor Success',
  props<{ deskriptor: Deskriptor[] }>()
);

export const loadDeskriptorFailure = createAction(
  '[Deskriptor] Load Deskriptor Failure',
  props<{ error: any }>()
);
