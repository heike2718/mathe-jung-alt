import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { createAction, props } from "@ngrx/store";
import { SUCHKONTEXT } from "../entities/suchfilter";

export const suchkontextChanged = createAction(
  '[Suchfilter] kontext changed',
  props<{kontext: SUCHKONTEXT}>()
);

export const suchstringChanged = createAction(
    '[Suchfilter] suchtring changed',
    props<{suchstring: string}>()
);

export const deskriptorAdded = createAction(
    '[Suchfilter] Deskriptor Added To Search List',
    props<{deskriptor: Deskriptor}>()
  );
  
  export const deskriptorRemoved = createAction(
    '[Suchfilter] Deskriptor Removed From Search List',
    props<{deskriptor: Deskriptor}>()
  );
  