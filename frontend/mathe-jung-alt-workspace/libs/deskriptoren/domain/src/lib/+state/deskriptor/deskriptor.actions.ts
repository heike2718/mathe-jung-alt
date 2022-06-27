import { createAction, props } from '@ngrx/store';
import { Deskriptor } from '../../entities/deskriptor';

export const loadDeskriptoren = createAction(
  '[Deskriptor] Load Deskriptor');

export const loadDeskriptorenSuccess = createAction(
  '[Deskriptor] Load Deskriptor Success',
  props<{ deskriptor: Deskriptor[] }>()
);
