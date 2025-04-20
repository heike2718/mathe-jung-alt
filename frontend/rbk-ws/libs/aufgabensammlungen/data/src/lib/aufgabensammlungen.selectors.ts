import { createSelector } from "@ngrx/store";
import { aufgabensammlungenFeature } from "./aufgabensammlungen.reducer";

const { selectAufgabensammlungenState: selectAufgabensammlungenState } = aufgabensammlungenFeature;

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

const suchparameter = createSelector(
    selectAufgabensammlungenState,
    (state) => state.suchparameter
);

const aufgabensammlungDetails = createSelector(
    selectAufgabensammlungenState,
    (state) =>  state.aufgabensammlungDetails
);

const aufgabensammlungBasisdaten = createSelector(
    selectAufgabensammlungenState,
    (state) => state.aufgabensammlungBasisdaten
);

const aufgabensammlungselemente = createSelector(
    aufgabensammlungDetails,
    (details) => details ? details.elemente : []
);

const selectedAufgabensammlungselement = createSelector(
    selectAufgabensammlungenState,
    (state) => state.selectedAufgabensammlungselement
);

const selectedElementImages = createSelector(
    selectAufgabensammlungenState,
    (state) => state.selectedElementImages
);

export const fromAufgabensammlungen = {
    isLoaded,
    anzahlTrefferGesamt,
    page,
    paginationState,
    suchparameter,
    aufgabensammlungBasisdaten: aufgabensammlungBasisdaten,
    aufgabensammlungDetails: aufgabensammlungDetails,
    aufgabensammlungselemente,
    selectedAufgabensammlungselement,
    selectedElementImages
};

