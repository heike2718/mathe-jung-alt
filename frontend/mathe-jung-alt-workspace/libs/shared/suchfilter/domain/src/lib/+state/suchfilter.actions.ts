import { Deskriptor, Suchkontext } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { createAction, props } from "@ngrx/store";


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
