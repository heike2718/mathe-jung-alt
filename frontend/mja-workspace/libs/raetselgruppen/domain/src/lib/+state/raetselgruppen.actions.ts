import { PageDefinition } from "@mja-workspace/suchfilter/domain";
import { createAction, props } from "@ngrx/store";
import { RaetselgruppensucheTreffer, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";


export const suchparameterChanged = createAction(
    '[Raetselgruppen] suchparameterChanged',
    props<{ suchparameter: RaetselgruppenSuchparameter }>()
);

export const selectRaetselgruppe = createAction(
    '[Raetselgruppen] selectRaetselgruppe',
    props<{ raetselgruppe: RaetselgruppensucheTrefferItem }>()
);

export const pageLoaded = createAction(
    '[Raetselgruppen] loadPage',
    props<{treffer: RaetselgruppensucheTreffer}>()
);



