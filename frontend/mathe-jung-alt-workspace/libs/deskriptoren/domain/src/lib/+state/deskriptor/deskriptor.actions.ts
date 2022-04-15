import { createAction, props } from '@ngrx/store';
import { Deskriptor } from '../../entities/deskriptor';

export const loadDeskriptoren = createAction('[Deskriptor] Load Deskriptor');

export const loadDeskriptorenSuccess = createAction(
  '[Deskriptor] Load Deskriptor Success',
  props<{ deskriptor: Deskriptor[] }>()
);

export const loadDeskriptorenFailure = createAction(
  '[Deskriptor] Load Deskriptor Failure',
  props<{ error: any }>()
);

export const deskriptorAddedToSearchList = createAction(
  '[Deskriptor] Deskriptor Added To Search List',
  props<{deskriptor: Deskriptor}>()
);

export const deskriptorRemovedFromSearchList = createAction(
  '[Deskriptor] Deskriptor Removed From Search List',
  props<{deskriptor: Deskriptor}>()
);
