import { createSelector } from "@ngrx/store";
import { medienFeature } from "./medien.reducer";


const { selectMedienState: selectMedienState} = medienFeature;

const isLoaded = createSelector(
    selectMedienState,
    state => state.loaded
);

const anzahlTrefferGesamt = createSelector(
    selectMedienState,
    state => state.anzahlTrefferGesamt
);

const page = createSelector(
    selectMedienState,
    state => state.page
);

const paginationState = createSelector(
    selectMedienState,
    state => state.paginationState
);

const selectedTrefferItem = createSelector(
    selectMedienState, 
    state => state.selectedTrefferItem
);

const selectedMediumDetails = createSelector(
    selectMedienState,
    state => state.selectedMediumDetails
);

const allMedienDetails = createSelector(
    selectMedienState,
    state => state.allMedienDetails
);


export const fromMedien = {
    isLoaded,
    anzahlTrefferGesamt,
    page,
    paginationState,
    selectedTrefferItem,
    selectedMediumDetails,
    allMedienDetails
};
