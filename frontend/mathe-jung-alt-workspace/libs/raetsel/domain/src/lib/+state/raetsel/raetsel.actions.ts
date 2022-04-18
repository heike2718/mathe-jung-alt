import { Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { createAction, props } from '@ngrx/store';
import { Raetsel } from '../../entities/raetsel';

export const findRaetsel = createAction('[Raetsel] find Raetsel',
  props<{ suchfilter: Suchfilter }>());


export const findRaetselSuccess = createAction(
  '[Raetsel] find Raetsel Success',
  props<{ raetsel: Raetsel[] }>()
);

export const selectPage = createAction('[Raetsel] select Page',
  props<{ sortDirection: string, pageIndex: number, pageSize: number }>());

export const pageSelected = createAction('[Raetsel] page selected',
  props<{ raetsel: Raetsel[] }>()
);

export const raetsellisteCleared = createAction('[Raetsel] raetselliste cleared ');

