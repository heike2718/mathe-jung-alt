import { createAction, props } from "@ngrx/store";
import { EditRaetselgruppenelementPayload, EditRaetselgruppePayload, RaetselgruppeBasisdaten, RaetselgruppeDetails, RaetselgruppensucheTreffer, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";


export const suchparameterChanged = createAction(
    '[Raetselgruppen] suchparameterChanged',
    props<{ suchparameter: RaetselgruppenSuchparameter }>()
);

export const selectRaetselgruppe = createAction(
    '[Raetselgruppen] selectRaetselgruppe',
    props<{ raetselgruppe: RaetselgruppensucheTrefferItem }>()
);

export const unselectRaetselgruppe = createAction(
    '[Raetselgruppen] unselectRaetselgruppe');


export const raetselgruppeDetailsLoaded = createAction(
    '[Raetselgruppen] details loaded',
    props<{ raetraetselgruppeDetails: RaetselgruppeDetails }>()
);

export const showRaetselgruppeDetails = createAction(
    '[Raetselgruppen] show details'
);

export const pageLoaded = createAction(
    '[Raetselgruppen] loadPage',
    props<{ treffer: RaetselgruppensucheTreffer }>()
);

export const editRaetselgruppe = createAction(
    '[Raetselgruppen] editRaetselgruppe',
    props<{ raetselgruppeBasisdaten: RaetselgruppeBasisdaten }>()
)

export const startSaveRaetselgruppe = createAction(
    '[Raetselgruppen] startSaveRaetselgruppe',
    props<{ editRaetselgruppePayload: EditRaetselgruppePayload }>()
);

export const saveRaetselgruppe = createAction(
    '[Raetselgruppen] saveRaetselgruppe',
    props<{ editRaetselgruppePayload: EditRaetselgruppePayload, csrfToken: string | null }>()
);

export const raetselgruppeSaved = createAction(
    '[Raetselgruppen] raetselgruppeSaved',
    props<{ raetselgruppe: RaetselgruppeBasisdaten }>()
);

export const saveRaetselgruppenelement = createAction(
    '[Raetselgruppen] saveRaetselgruppenelement',
    props<{ raetselgruppeID: string, payload: EditRaetselgruppenelementPayload }>()
);

export const raetselgruppenelementeChanged = createAction(
    '[Raetselgruppen] raetselgruppenelementeChanged',
    props<{ raetraetselgruppeDetails: RaetselgruppeDetails }>()
);

export const deleteRaetselgruppenelement = createAction(
    '[Raetselgruppen] deleteRaetselgruppenelement',
    props<{raetselgruppeID: string, payload: EditRaetselgruppenelementPayload}>()
);




