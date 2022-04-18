import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { createAction, props } from "@ngrx/store";
import { Suchkontext } from "../entities/suchfilter";

export const suchkontextChanged = createAction(
  '[Suchfilter] kontext changed',
  props<{ kontext: Suchkontext }>()
);

export const suchstringChanged = createAction(
  '[Suchfilter] suchtring changed',
  props<{ suchstring: string }>()
);

export const deskriptorenChanged = createAction(
  '[Suchfilter] deskriptoren changed',
  props<{deskriptoren: Deskriptor[]}>()
);
