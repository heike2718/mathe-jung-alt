import { Deskriptor, filterByKontext } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { createReducer, on, Action } from '@ngrx/store';
import { Suchkontext } from '../entities/suchfilter';
import * as SuchfilterActions from './suchfilter.actions';


export const SUCHFILTER_FEATURE_KEY = 'suchfilter';

export interface SuchfilterPartialState {
    readonly [SUCHFILTER_FEATURE_KEY]: SuchfilterState;
};

export interface SuchfilterState {
    readonly kontext: Suchkontext;
    readonly deskriptoren: Deskriptor[];
    readonly suchstring: string;
    readonly filteredDeskriptoren: Deskriptor[],
    readonly suchliste: Deskriptor[];
    readonly deskriptorenLoaded: boolean;
}

const initialState: SuchfilterState = {
    kontext: 'NOOP',
    deskriptoren: [],
    suchstring: '',
    filteredDeskriptoren: [],
    suchliste: [],
    deskriptorenLoaded: false
};

const suchfilterReducer = createReducer(
    initialState,

    on(SuchfilterActions.loadDeskriptorenSuccess, (state, action) => {
        return {...state, deskriptoren: action.deskriptoren, deskriptorenLoaded: true}
    }),

    on(SuchfilterActions.suchkontextChanged, (state, action) => {

        let filteredDeskriptorenNeu: Deskriptor[] = filterByKontext(action.kontext, state.deskriptoren);
       
        return {...state, kontext: action.kontext, filteredDeskriptoren: filteredDeskriptorenNeu, suchliste: [], suchstring: ''};
    }),

    on(SuchfilterActions.suchstringChanged, (state, action) => {

        return {...state, suchstring: action.suchstring};
    }),

    on(SuchfilterActions.deskriptorenChanged, (state, action) => {

        return { ...state, suchliste: action.deskriptoren };
    }), 

    on(SuchfilterActions.deskriptorAddedToSearchList, (state, action) => {

        const filtederDeskriptorenNeu = state.filteredDeskriptoren.filter(d => d.id !== action.deskriptor.id);
        return {...state, filteredDeskriptoren: filtederDeskriptorenNeu, suchliste: [...state.suchliste, action.deskriptor]}

    }),

    on (SuchfilterActions.deskriptorRemovedFromSearchList, (state, action) => {
        const suchlisteNeu = state.suchliste.filter(d => d.id !== action.deskriptor.id);
        return {...state, filteredDeskriptoren: [...state.filteredDeskriptoren, action.deskriptor], suchliste: suchlisteNeu};
    }),

    on(SuchfilterActions.reset, (_state, _action) => {
        return initialState;
    })
);

export function reducer(state: SuchfilterState | undefined, action: Action) {
    return suchfilterReducer(state, action);
};
