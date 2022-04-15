import { createAction, props } from '@ngrx/store';
import { Raetsel } from '../../entities/raetsel';

export const findRaetsel = createAction('[Raetsel] Find Raetsel',
  props<{ filter: string }>());


export const findRaetselSuccess = createAction(
  '[Raetsel] Find Raetsel Success',
  props<{ raetsel: Raetsel[] }>()
);

export const selectPage = createAction('[Raetsel] Select Page',
  props<{ sortDirection: string, pageIndex: number, pageSize: number }>());

export const pageSelected = createAction('[Raetsel] Page selected',
  props<{ raetsel: Raetsel[] }>()
);

