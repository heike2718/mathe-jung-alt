import { PageDefinition, Suchfilter, Suchkontext } from '@mja-workspace/suchfilter/domain';
import { createAction, props } from '@ngrx/store';
import { Raetsel } from '../../entities/raetsel';

export const prepareSearch = createAction(
  '[Raetsel] prepareSearch',
  props<{ suchfilter: Suchfilter, pageDefinition: PageDefinition }>()
);

export const selectPage = createAction(
  '[Raetsel] setPageDefinition',
  props<{ pageDefinition: PageDefinition }>()
);

export const countRaetsel = createAction(
  '[Raetsel] count'
);

export const raetselCounted = createAction(
  '[Raetsel] count completed',
  props<{ anzahl: number }>()
);

export const findRaetsel = createAction(
  '[Raetsel] find Raetsel',
  props<{ suchfilter: Suchfilter, pageDefinition: PageDefinition, kontext: Suchkontext }>()
);

export const findRaetselSuccess = createAction(
  '[Raetsel] find Raetsel Success',
  props<{ raetsel: Raetsel[] }>()
);

export const pageSelected = createAction(
  '[Raetsel] page selected',
  props<{ raetsel: Raetsel[] }>()
);

export const raetsellisteCleared = createAction(
  '[Raetsel] raetselliste cleared '
);
