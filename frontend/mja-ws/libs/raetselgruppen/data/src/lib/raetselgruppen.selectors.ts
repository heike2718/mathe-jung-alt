import { createSelector } from "@ngrx/store";
import { raetselgruppenFeature } from "./raetselgruppen.reducer";

const { selectRaetselgruppenState } = raetselgruppenFeature;

const isLoaded = createSelector(
    selectRaetselgruppenState,
    (state) => state.loaded
);

const anzahlTrefferGesamt = createSelector(
    selectRaetselgruppenState,
    (state) => state.anzahlTrefferGesamt
)

const page = createSelector(
    selectRaetselgruppenState,
    (state) => state.page
);

const paginationState = createSelector(
    selectRaetselgruppenState,
    (state) => state.paginationState
);

const raetselgruppeDetails = createSelector(
    selectRaetselgruppenState,
    (state) => state.raetselgruppeDetails
);

const raetselgruppeBasisdaten = createSelector(
    selectRaetselgruppenState,
    (state) => state.raetselgruppeBasisdaten
);

const raetselgruppenelemente = createSelector(
    raetselgruppeDetails,
    (details) => details ? details.elemente : []
);

export const fromRaetselgruppen = {
    isLoaded,
    anzahlTrefferGesamt,
    page,
    paginationState,
    raetselgruppeBasisdaten,
    raetselgruppeDetails,
    raetselgruppenelemente
};

