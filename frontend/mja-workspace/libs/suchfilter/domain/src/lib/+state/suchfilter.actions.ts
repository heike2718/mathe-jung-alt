import { createAction, props } from "@ngrx/store";
import { Deskriptor, Suchfilter, SuchfilterUIModel, Suchkontext } from "../entities/suchfilter";

export const loadDeskriptoren = createAction(
  '[Suchfilter] load Deskriptoren');

export const loadDeskriptorenSuccess = createAction(
  '[Suchfilter] load Deskriptoren Success',
  props<{ deskriptoren: Deskriptor[] }>()
);

export const setSuchfilter = createAction(
  '[Suchfilter] setSuchfilter',
  props<{suchfilter: Suchfilter}>()
);

// export const suchkontextChanged = createAction(
//   '[Suchfilter] kontext changed',
//   props<{ kontext: Suchkontext }>()
// );

export const suchstringChanged = createAction(
  '[Suchfilter] suchtring changed',
  props<{ suchstring: string }>()
);

export const deskriptorAddedToSearchList = createAction(
  '[Suchfilter] Deskriptor added to search list',
  props<{deskriptor: Deskriptor}>()
);

export const deskriptorRemovedFromSearchList = createAction(
  '[Suchfilter] Deskriptor removed from search list',
  props<{deskriptor: Deskriptor}>()
);

export const markUnchanged = createAction(
  '[Suchfilter] unchanged',
  props<{ kontext: Suchkontext }>()
);

export const resetKontext = createAction(
  '[Suchfilter] reset kontext',
  props<{ kontext: Suchkontext }>()
);

export const reset = createAction(
  '[Suchfilter] reset all'
);
