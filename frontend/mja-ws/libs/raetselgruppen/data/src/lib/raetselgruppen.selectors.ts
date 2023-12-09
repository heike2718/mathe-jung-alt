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

// aus irgendwelchen Gründen ist nach F5 der ganze state undefined, obwohl es genauso aufgebaut ist wie in der raetsel-lib. 

const raetselgruppeDetails = createSelector(
    selectRaetselgruppenState,
    (state) => state ? state.raetselgruppeDetails : undefined
);

const raetselgruppeBasisdaten = createSelector(
    selectRaetselgruppenState,
    (state) => state ? state.raetselgruppeBasisdaten : undefined
);

const raetselgruppenelemente = createSelector(
    raetselgruppeDetails,
    (details) => details ? details.elemente : []
);

const selectedRaetselgruppenelement = createSelector(
    selectRaetselgruppenState,
    (state) => state ? state.selectedRaetselgruppenelement : undefined
);

const selectedElementImages = createSelector(
    selectRaetselgruppenState,
    (state) => state ? state.selectedElementImages : undefined
);

export const fromRaetselgruppen = {
    isLoaded,
    anzahlTrefferGesamt,
    page,
    paginationState,
    raetselgruppeBasisdaten,
    raetselgruppeDetails,
    raetselgruppenelemente,
    selectedRaetselgruppenelement,
    selectedElementImages
};

