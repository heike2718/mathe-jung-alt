import { createSelector } from "@ngrx/store";
import { aufgabensammlungenFeature } from "./aufgabensammlungen.reducer";

const { selectRaetselgruppenState: selectAufgabensammlungenState } = aufgabensammlungenFeature;

const isLoaded = createSelector(
    selectAufgabensammlungenState,
    (state) => state.loaded
);

const anzahlTrefferGesamt = createSelector(
    selectAufgabensammlungenState,
    (state) => state.anzahlTrefferGesamt
)

const page = createSelector(
    selectAufgabensammlungenState,
    (state) => state.page
);

const paginationState = createSelector(
    selectAufgabensammlungenState,
    (state) => state.paginationState
);

// aus irgendwelchen Gründen ist nach F5 der ganze state undefined, obwohl es genauso aufgebaut ist wie in der raetsel-lib. 

const aufgabensammlungDetails = createSelector(
    selectAufgabensammlungenState,
    (state) => state ? state.aufgabensammlungDetails : undefined
);

const aufgabensammlungBasisdaten = createSelector(
    selectAufgabensammlungenState,
    (state) => state ? state.aufgabensammlungBasisdaten : undefined
);

const aufgabensammlungselemente = createSelector(
    aufgabensammlungDetails,
    (details) => details ? details.elemente : []
);

const selectedAufgabensammlungselement = createSelector(
    selectAufgabensammlungenState,
    (state) => state ? state.selectedAufgabensammlungselement : undefined
);

const selectedElementImages = createSelector(
    selectAufgabensammlungenState,
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

