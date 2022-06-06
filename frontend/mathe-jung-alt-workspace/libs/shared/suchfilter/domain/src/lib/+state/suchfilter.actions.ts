import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { createAction, props } from "@ngrx/store";
import { Suchkontext } from "../entities/suchfilter";

export const loadDeskriptoren = createAction(
  '[Suchfilter] load Deskriptoren'
);

export const loadDeskriptorenSuccess = createAction(
  '[Suchfilter] Deskriptoren loaded',
  props<{ deskriptoren: Deskriptor[] }>()
);

export const suchkontextChanged = createAction(
  '[Suchfilter] kontext changed',
  props<{ kontext: Suchkontext }>()
);

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

export const reset = createAction(
  '[Suchfilter] reset all'
);
