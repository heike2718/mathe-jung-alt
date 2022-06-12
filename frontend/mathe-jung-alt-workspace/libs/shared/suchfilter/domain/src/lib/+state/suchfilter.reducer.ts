import { Deskriptor, filterByKontext } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { createReducer, on, Action } from '@ngrx/store';
import { createInitialSuchfilterUIModels, findSuchfilterUIModelWithKontext, initialSuchfilter, SuchfilterUIModel, Suchkontext } from '../entities/suchfilter';
import * as SuchfilterActions from './suchfilter.actions';


export const SUCHFILTER_FEATURE_KEY = 'suchfilter';

export interface SuchfilterPartialState {
    readonly [SUCHFILTER_FEATURE_KEY]: SuchfilterState;
};

export interface SuchfilterState {
    readonly kontext: Suchkontext;
    readonly deskriptoren: Deskriptor[];
    readonly deskriptorenLoaded: boolean;
    readonly suchfilterUIModels: SuchfilterUIModel[];
}

const initialState: SuchfilterState = {
    kontext: 'NOOP',
    deskriptoren: [],
    deskriptorenLoaded: false,
    suchfilterUIModels: [{ suchfilter: initialSuchfilter, filteredDeskriptoren: [], changed: false }]
};

const suchfilterReducer = createReducer(
    initialState,

    on(SuchfilterActions.loadDeskriptorenSuccess, (state, action) => {

        const uiModel = createInitialSuchfilterUIModels(action.deskriptoren);
        return { ...state, deskriptoren: action.deskriptoren, deskriptorenLoaded: true, suchfilterUIModels: uiModel };
    }),

    on(SuchfilterActions.suchkontextChanged, (state, action) => {

        const selectedSuchfilterUIModel = findSuchfilterUIModelWithKontext(state.kontext, state.suchfilterUIModels);

        if (!selectedSuchfilterUIModel || selectedSuchfilterUIModel.suchfilter.kontext === action.kontext) {
            return {...state};
        }

        const neueUIModels: SuchfilterUIModel[] = [];
        state.suchfilterUIModels.forEach(m => {
            if (action.kontext === m.suchfilter.kontext) {
                neueUIModels.push({...m, changed: false});
            } else {
                neueUIModels.push({...m});
            }
        })

        return { ...state, kontext: action.kontext, suchfilterUIModels: neueUIModels };
    }),

    on(SuchfilterActions.suchstringChanged, (state, action) => {


        const selectedSuchfilterUIModel = findSuchfilterUIModelWithKontext(state.kontext, state.suchfilterUIModels);

        if (!selectedSuchfilterUIModel) {
            return { ...state };
        }

        const newUiModels: SuchfilterUIModel[] = [];
        const neuerSuchfilter = {
            ...selectedSuchfilterUIModel.suchfilter,
            suchstring: action.suchstring,
            
        };

        state.suchfilterUIModels.forEach( f => {
            if (f.suchfilter.kontext === neuerSuchfilter.kontext) {
                newUiModels.push({...selectedSuchfilterUIModel, suchfilter: neuerSuchfilter, changed: true});
            } else {
                newUiModels.push({...f});
            }
        });

        return { ...state, suchfilterUIModels: newUiModels };
    }),

    on(SuchfilterActions.deskriptorAddedToSearchList, (state, action) => {

        const selectedSuchfilterUIModel = findSuchfilterUIModelWithKontext(state.kontext, state.suchfilterUIModels);

        if (!selectedSuchfilterUIModel) {
            return { ...state };
        }

        const newUiModels: SuchfilterUIModel[] = [];
        const neuerSuchfilter = {
            ...selectedSuchfilterUIModel.suchfilter,
            deskriptoren: [...selectedSuchfilterUIModel.suchfilter.deskriptoren, action.deskriptor]
        };

        state.suchfilterUIModels.forEach( f => {
            if (f.suchfilter.kontext === neuerSuchfilter.kontext) {
                newUiModels.push({...selectedSuchfilterUIModel, suchfilter: neuerSuchfilter, changed: true});
            } else {
                newUiModels.push({...f});
            }
        });

        return { ...state, suchfilterUIModels: newUiModels };
    }),

    on(SuchfilterActions.deskriptorRemovedFromSearchList, (state, action) => {

        const selectedSuchfilterUIModel = findSuchfilterUIModelWithKontext(state.kontext, state.suchfilterUIModels);

        if (!selectedSuchfilterUIModel) {
            return { ...state };
        }

        const newUiModels: SuchfilterUIModel[] = [];
        const suchlisteNeu = selectedSuchfilterUIModel.suchfilter.deskriptoren.filter(d => d.id !== action.deskriptor.id);
        const neuerSuchfilter = {
            ...selectedSuchfilterUIModel.suchfilter,
            deskriptoren: suchlisteNeu
        };

        state.suchfilterUIModels.forEach( f => {
            if (f.suchfilter.kontext === neuerSuchfilter.kontext) {
                newUiModels.push({...selectedSuchfilterUIModel, suchfilter: neuerSuchfilter, changed: true});
            } else {
                newUiModels.push({...f});
            }
        });

        return { ...state, suchfilterUIModels: newUiModels };
    }),

    on(SuchfilterActions.markUnchanged, (state, action) => {

        const selectedSuchfilterUIModel = findSuchfilterUIModelWithKontext(state.kontext, state.suchfilterUIModels);

        if (!selectedSuchfilterUIModel) {
            return { ...state };
        }

        const newUiModels: SuchfilterUIModel[] = [];
        state.suchfilterUIModels.forEach( f => {
            if (f.suchfilter.kontext === action.kontext) {
                newUiModels.push({...selectedSuchfilterUIModel, changed: false});
            } else {
                newUiModels.push({...f});
            }
        });

        return { ...state, suchfilterUIModels: newUiModels };

        return {...state};
    }),

    on(SuchfilterActions.reset, (_state, _action) => {
        return initialState;
    })
);

export function reducer(state: SuchfilterState | undefined, action: Action) {
    return suchfilterReducer(state, action);
};
