import { GeneratedImages, initialPaginationState, PaginationState, SelectableItem } from "@mja-ws/core/model";
import { Raetsel, RaetselDetails } from "@mja-ws/raetsel/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { raetselActions } from "./raetsel.actions";

export interface RaetselState {
    readonly loaded: boolean;
    readonly page: Raetsel[];
    readonly paginationState: PaginationState;
    readonly selectableDeskriptoren: SelectableItem[];
    readonly saveSuccessMessage: string | undefined;
    readonly raetselDetails: RaetselDetails | undefined;
};

const initialState: RaetselState = {
    loaded: false,
    page: [],
    paginationState: initialPaginationState,
    selectableDeskriptoren: [],
    saveSuccessMessage: undefined,
    raetselDetails: undefined
};

export const raetselFeature = createFeature({
    name: 'raetsel',
    reducer: createReducer(
        initialState,
        on(raetselActions.select_page, (state, action): RaetselState => {
            return {
                ...state,
                paginationState: { ...state.paginationState, pageDefinition: { pageIndex: action.pageDefinition.pageIndex, pageSize: action.pageDefinition.pageSize, sortDirection: action.pageDefinition.sortDirection } }
            };
        }),
        on(raetselActions.raetsel_found, (state, action): RaetselState => {
            return {
                ...state,
                loaded: true,
                page: action.treffer.treffer,
                paginationState: { ...state.paginationState, anzahlTreffer: action.treffer.trefferGesamt },
                raetselDetails: undefined
            }
        }),
        on(raetselActions.raetsel_details_loaded, (state, action) => {

            const selectableDeskriptoren: SelectableItem[] = [];
            action.raetselDetails.deskriptoren.forEach(d => {
                selectableDeskriptoren.push({
                    id: d.id,
                    name: d.name,
                    selected: true
                })
            });

            return {
                ...state,
                raetselDetails: action.raetselDetails,
                selectableDeskriptoren: selectableDeskriptoren
            };
        }),
        on(raetselActions.raetsel_png_generated, (state, action) => {

            if (state.raetselDetails) {
                const images: GeneratedImages = { imageFrage: action.images.imageFrage, imageLoesung: action.images.imageLoesung };
                const neueDetails: RaetselDetails = { ...state.raetselDetails, images: images };
                return { ...state, raetselDetails: neueDetails, generatingOutput: false };
            }

            return { ...state };
        }),
        on(raetselActions.raetsel_saved, (state, action) => {

            const selectableDeskriptoren: SelectableItem[] = [];
            action.raetselDetails.deskriptoren.forEach(d => {
                selectableDeskriptoren.push({
                    id: d.id,
                    name: d.name,
                    selected: true
                })
            });

            return {
                ...state,
                raetselDetails: action.raetselDetails,
                selectableDeskriptoren: selectableDeskriptoren
            };
        }),
        on(raetselActions.raetselliste_cleared, (state, _action): RaetselState => {
            return {
                ...state,
                loaded: false,
                page: [],
                raetselDetails: undefined
            };
        }),
        on(raetselActions.raetsel_cancel_selection, (state, _action) => {
            return {...state, raetselDetails: undefined, selectableDeskriptoren: []}
        })
    )
});
