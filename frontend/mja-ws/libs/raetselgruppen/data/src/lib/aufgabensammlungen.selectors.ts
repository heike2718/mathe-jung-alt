import { createSelector } from "@ngrx/store";
import { raetselgruppenFeature } from "./aufgabensammlungen.reducer";

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

const aufgabensammlungDetails = createSelector(
    selectRaetselgruppenState,
    (state) => state ? state.aufgabensammlungDetails : undefined
);

const aufgabensammlungBasisdaten = createSelector(
    selectRaetselgruppenState,
    (state) => state ? state.aufgabensammlungBasisdaten : undefined
);

const aufgabensammlungselemente = createSelector(
    aufgabensammlungDetails,
    (details) => details ? details.elemente : []
);

const selectedAufgabensammlungselement = createSelector(
    selectRaetselgruppenState,
    (state) => state ? state.selectedAufgabensammlungselement : undefined
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
    aufgabensammlungBasisdaten: aufgabensammlungBasisdaten,
    aufgabensammlungDetails: aufgabensammlungDetails,
    aufgabensammlungselemente,
    selectedAufgabensammlungselement,
    selectedElementImages
};

