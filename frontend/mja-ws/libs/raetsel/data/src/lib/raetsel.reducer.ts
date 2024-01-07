import { GeneratedImages, initialPaginationState, PaginationState, SelectableItem } from "@mja-ws/core/model";
import { initialRaetselSuchfilter, Raetsel, RaetselDetails, RaetselSuchfilter, MediumQuelleDto, GUIEditRaetselPayload, createEditRaetselPayload, LinkedAufgabensammlung } from "@mja-ws/raetsel/model";
import { createFeature, createReducer, on } from "@ngrx/store";
import { raetselActions } from "./raetsel.actions";
import { swallowEmptyArgument } from "@mja-ws/shared/util";
export interface RaetselState {
    readonly loaded: boolean;
    readonly page: Raetsel[];
    readonly paginationState: PaginationState;
    readonly selectableDeskriptoren: SelectableItem[];
    readonly raetselDetails: RaetselDetails | undefined;
    readonly raetselSuchfilter: RaetselSuchfilter;
    readonly generateLatexError: boolean;
    readonly medienForQuelle: MediumQuelleDto[];
    readonly guiEditRaetselPayload: GUIEditRaetselPayload | undefined;
    readonly linkedAufgabensammlungen: LinkedAufgabensammlung[];
};

const initialState: RaetselState = {
    loaded: false,
    page: [],
    paginationState: initialPaginationState,
    selectableDeskriptoren: [],
    raetselDetails: undefined,
    raetselSuchfilter: initialRaetselSuchfilter,
    generateLatexError: false,
    medienForQuelle: [],
    guiEditRaetselPayload: undefined,
    linkedAufgabensammlungen: []
};

export const raetselFeature = createFeature({
    name: 'raetsel',
    reducer: createReducer(
        initialState,
        on(raetselActions.rAETSEL_SELECT_PAGE, (state, action): RaetselState => {
            return {
                ...state,
                generateLatexError: false,
                paginationState: { ...state.paginationState, pageDefinition: { pageIndex: action.pageDefinition.pageIndex, pageSize: action.pageDefinition.pageSize, sortDirection: action.pageDefinition.sortDirection } }
            };
        }),
        on(raetselActions.rAETSELSUCHFILTER_CHANGED, (state, action) => {

            return { ...state, raetselSuchfilter: action.suchfilter, generateLatexError: false };
        }),
        on(raetselActions.rAETSEL_FOUND, (state, action): RaetselState => {
            return {
                ...state,
                loaded: true,
                page: action.treffer.treffer,
                paginationState: { ...state.paginationState, anzahlTreffer: action.treffer.trefferGesamt },
                raetselDetails: undefined,
                generateLatexError: false
            }
        }),
        on(raetselActions.rAETSEL_DETAILS_LOADED, (state, action) => {

            const selectableDeskriptoren: SelectableItem[] = [];
            action.raetselDetails.deskriptoren.forEach(d => {
                selectableDeskriptoren.push({
                    id: d.id,
                    name: d.name,
                    selected: true
                })
            });

            const theNewEditRaetselPayload = createEditRaetselPayload(action.raetselDetails);

            return {
                ...state,
                raetselDetails: action.raetselDetails,
                selectableDeskriptoren: selectableDeskriptoren,
                generateLatexError: false,
                guiEditRaetselPayload: {
                    ...state.guiEditRaetselPayload,
                    editRaetselPayload: theNewEditRaetselPayload.editRaetselPayload,
                    embeddableImageInfos: theNewEditRaetselPayload.embeddableImageInfos,
                    quellenangabe: theNewEditRaetselPayload.quellenangabe
                }
            };
        }),
        on(raetselActions.iNIT_EDIT_RAETSEL_PAYLOD, (state, action) => {
            return { ...state, guiEditRaetselPayload: action.payload };
        }),

        on(raetselActions.rAETSEL_PNG_GENERATED, (state, action) => {

            if (state.raetselDetails) {
                const images: GeneratedImages = { imageFrage: action.images.imageFrage, imageLoesung: action.images.imageLoesung };
                const neueDetails: RaetselDetails = { ...state.raetselDetails, images: images };
                return { ...state, raetselDetails: neueDetails, generatingOutput: false, generateLatexError: false };
            }

            return { ...state };
        }),
        on(raetselActions.rAETSEL_SAVED, (state, action) => {

            const selectableDeskriptoren: SelectableItem[] = [];
            action.raetselDetails.deskriptoren.forEach(d => {
                selectableDeskriptoren.push({
                    id: d.id,
                    name: d.name,
                    selected: true
                })
            });

            const theNewEditRaetselPayload = createEditRaetselPayload(action.raetselDetails);

            return {
                ...state,
                raetselDetails: action.raetselDetails,
                selectableDeskriptoren: selectableDeskriptoren,
                generateLatexError: false,
                guiEditRaetselPayload: {
                    ...state.guiEditRaetselPayload,
                    editRaetselPayload: theNewEditRaetselPayload.editRaetselPayload,
                    embeddableImageInfos: theNewEditRaetselPayload.embeddableImageInfos,
                    quellenangabe: theNewEditRaetselPayload.quellenangabe
                }
            };
        }),

        on(raetselActions.rESET_RAETSELSUCHFILTER, (state, action): RaetselState => {

            swallowEmptyArgument(action, false);

            return {
                ...state,
                loaded: false,
                page: [],
                raetselDetails: undefined,
                raetselSuchfilter: initialRaetselSuchfilter,
                generateLatexError: false,
                paginationState: initialPaginationState
            };
        }),
        on(raetselActions.rAETSEL_CANCEL_SELECTION, (state, action) => {
            swallowEmptyArgument(action, false);
            return {
                ...state,
                raetselDetails: undefined,
                selectableDeskriptoren: [],
                generateLatexError: false,
                guiEditRaetselPayload: undefined,
                medienForQuelle: [],
                linkedAufgabensammlungen: []
            }
        }),
        on(raetselActions.lATEX_ERRORS_DETECTED, (state, action) => {
            swallowEmptyArgument(action, false);
            return {
                ...state,
                generateLatexError: true
            }
        }),
        on(raetselActions.mEDIEN_FOR_QUELLE_FOUND, (state, action) => {
            return {
                ...state,
                medienForQuelle: action.result
            };
        }),
        on(raetselActions.lINKED_AUFGABENSAMMLUNGEN_FOUND, (state, action) => {
            return {
                ...state,
                linkedAufgabensammlungen: action.linkedAufgabensammlungen
            };
        }),
        
    )
});
