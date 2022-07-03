import { createReducer, on, Action } from '@ngrx/store';
import { Deskriptor, findSuchfilterUIModelWithKontext, initialSuchfilter, Suchfilter, SuchfilterUIModel, Suchkontext } from '../entities/suchfilter';
import * as SuchfilterActions from './suchfilter.actions';


export const SUCHFILTER_FEATURE_KEY = 'suchfilter';

export interface SuchfilterPartialState {
    readonly [SUCHFILTER_FEATURE_KEY]: SuchfilterState;
};

export interface SuchfilterState {
    readonly loaded: boolean;
    readonly deskriptoren: Deskriptor[],
    readonly kontext: Suchkontext;
    readonly suchfilterUIModels: SuchfilterUIModel[];
}

const initialState: SuchfilterState = {
    loaded: false,
    deskriptoren: [],
    kontext: 'NOOP',
    suchfilterUIModels: [{ suchfilter: initialSuchfilter, filteredDeskriptoren: [], changed: false }]
};

const suchfilterReducer = createReducer(
    initialState,
    
    on(SuchfilterActions.loadDeskriptorenSuccess, (state, action) => ({...state, deskriptoren: action.deskriptoren, loaded: true})),

    on(SuchfilterActions.suchkontextChanged, (state, action) => {

        const selectedSuchfilterUIModel = findSuchfilterUIModelWithKontext(state.kontext, state.suchfilterUIModels);

        if (!selectedSuchfilterUIModel || selectedSuchfilterUIModel.suchfilter.kontext === action.kontext) {
            return { ...state };
        }

        const neueUIModels: SuchfilterUIModel[] = [];
        state.suchfilterUIModels.forEach(m => {
            if (action.kontext === m.suchfilter.kontext) {
                neueUIModels.push({ ...m, changed: false });
            } else {
                neueUIModels.push({ ...m });
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

        state.suchfilterUIModels.forEach(f => {
            if (f.suchfilter.kontext === neuerSuchfilter.kontext) {
                newUiModels.push({ ...selectedSuchfilterUIModel, suchfilter: neuerSuchfilter, changed: true });
            } else {
                newUiModels.push({ ...f });
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

        state.suchfilterUIModels.forEach(f => {
            if (f.suchfilter.kontext === neuerSuchfilter.kontext) {
                newUiModels.push({ ...selectedSuchfilterUIModel, suchfilter: neuerSuchfilter, changed: true });
            } else {
                newUiModels.push({ ...f });
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

        state.suchfilterUIModels.forEach(f => {
            if (f.suchfilter.kontext === neuerSuchfilter.kontext) {
                newUiModels.push({ ...selectedSuchfilterUIModel, suchfilter: neuerSuchfilter, changed: true });
            } else {
                newUiModels.push({ ...f });
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
        state.suchfilterUIModels.forEach(f => {
            if (f.suchfilter.kontext === action.kontext) {
                newUiModels.push({ ...selectedSuchfilterUIModel, changed: false });
            } else {
                newUiModels.push({ ...f });
            }
        });

        return { ...state, suchfilterUIModels: newUiModels };
    }),

    on(SuchfilterActions.resetKontext, (state, action) => {

        const selectedSuchfilterUIModel = findSuchfilterUIModelWithKontext(state.kontext, state.suchfilterUIModels);
        const neuerSuchfilter: Suchfilter = {
            kontext: action.kontext,
            suchstring: '',
            deskriptoren: []
        };

        const newUiModels: SuchfilterUIModel[] = [];
        state.suchfilterUIModels.forEach(f => {
            if (f.suchfilter.kontext === action.kontext) {
                newUiModels.push({ ...selectedSuchfilterUIModel, filteredDeskriptoren: [], suchfilter: neuerSuchfilter, changed: false });
            } else {
                newUiModels.push({ ...f });
            }
        });
        return { ...state, suchfilterUIModels: newUiModels };
    }),

    on(SuchfilterActions.reset, (_state, _action) => {
        return initialState;
    })
);

export function reducer(state: SuchfilterState | undefined, action: Action) {
    return suchfilterReducer(state, action);
};
