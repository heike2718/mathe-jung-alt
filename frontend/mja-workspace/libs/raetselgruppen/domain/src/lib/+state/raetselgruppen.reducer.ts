import { createReducer, on, Action } from '@ngrx/store';
import * as RaetselgruppenActions from './raetselgruppen.actions';

export const RAETSELGRUPPEN_FEATURE_KEY = 'raetselgruppen';

import { initialRaetselgruppenSuchparameter, RaetselgruppeBasisdaten, RaetselgruppeDetails, Raetselgruppenelement, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";


export interface RaetselgruppenState {
    readonly page: RaetselgruppensucheTrefferItem[];
    readonly anzahlTrefferGesamt: number;
    readonly suchparameter: RaetselgruppenSuchparameter,
    readonly selectedGruppe: RaetselgruppensucheTrefferItem | undefined;
    readonly raetselgruppeBasisdaten: RaetselgruppeBasisdaten | undefined;
    readonly raetselgruppeDetails: RaetselgruppeDetails | undefined;
};

export interface RaetselgruppenPartialState {
    readonly [RAETSELGRUPPEN_FEATURE_KEY]: RaetselgruppenState;
};

const initialState: RaetselgruppenState = {
    page: [],
    anzahlTrefferGesamt: 0,
    suchparameter: initialRaetselgruppenSuchparameter,
    selectedGruppe: undefined,
    raetselgruppeBasisdaten: undefined,
    raetselgruppeDetails: undefined
};

const raetselgruppenReducer = createReducer(
    initialState,

    on(RaetselgruppenActions.suchparameterChanged, (state, action) => {
        return {
            ...state,
            suchparameter: action.suchparameter
        };
    }),

    on(RaetselgruppenActions.pageLoaded, (state, action) => {
        return {
            ...state,
            page: action.treffer.items,
            anzahlTrefferGesamt: action.treffer.anzahlTreffer
        };
    }),

    on(RaetselgruppenActions.editRaetselgruppe, (state, action) => {

        // console.log(action.raetselgruppeBasisdaten);
        return { ...state, raetselgruppeBasisdaten: action.raetselgruppeBasisdaten };
    }),

    on(RaetselgruppenActions.raetselgruppeSaved, (state, action) => {
        const trefferItem: RaetselgruppensucheTrefferItem = action.raetselgruppe;
        return { ...state, raetselgruppeBasisdaten: trefferItem, selectedGruppe: trefferItem };
    }),

    on(RaetselgruppenActions.selectRaetselgruppe, (state, action) => ({ ...state, selectedGruppe: action.raetselgruppe })),

    on(RaetselgruppenActions.raetselgruppeDetailsLoaded, (state, action) => {

        // TODO: mocked data entfernen
        const elemente: Raetselgruppenelement[] = EXAMPLE_DATA;
        const neueDetails = action.raetraetselgruppeDetails;
        const details: RaetselgruppeDetails = {
            elemente: elemente,
            geaendertDurch: neueDetails.geaendertDurch,
            id: neueDetails.id,
            name: neueDetails.name,
            schwierigkeitsgrad: neueDetails.schwierigkeitsgrad,
            status: neueDetails.status,
            kommentar: neueDetails.kommentar,
            referenz: neueDetails.referenz,
            referenztyp: neueDetails.referenztyp
        };

        return { ...state, raetselgruppeDetails: details };
    }),

);

export function reducer(state: RaetselgruppenState | undefined, action: Action) {
    return raetselgruppenReducer(state, action);
};

// TODO: Replace this with your own data model type
const EXAMPLE_DATA: Raetselgruppenelement[] = [
    { id: '1', nummer: 'A-1', name: 'Treppenlift 20', punkte: 300, raetselSchluessel: '00001' },
    { id: '2', nummer: 'A-2', name: 'Treppenlift 2', punkte: 300, raetselSchluessel: '00002' },
    { id: '3', nummer: 'A-3', name: 'Treppenlift 23', punkte: 300, raetselSchluessel: '00003' },
    { id: '4', nummer: 'A-4', name: 'Treppenlift 14', punkte: 300, raetselSchluessel: '00004' },
    { id: '5', nummer: 'B-1', name: 'Treppenlift 5', punkte: 400, raetselSchluessel: '00005' },
    { id: '6', nummer: 'B-2', name: 'Treppenlift 6', punkte: 400, raetselSchluessel: '00006' },
    { id: '7', nummer: 'B-3', name: 'Treppenlift 7', punkte: 400, raetselSchluessel: '00007' },
    { id: '8', nummer: 'B-4', name: 'Treppenlift 8', punkte: 400, raetselSchluessel: '00008' },
    { id: '9', nummer: 'C-1', name: 'Treppenlift 9', punkte: 500, raetselSchluessel: '00009' },
    { id: '10', nummer: 'C-2', name: 'Treppenlift 10', punkte: 500, raetselSchluessel: '00010' },
    { id: '11', nummer: 'C-3', name: 'Treppenlift 21', punkte: 500, raetselSchluessel: '00011' },
    { id: '12', nummer: 'C-4', name: 'Treppenlift 11', punkte: 500, raetselSchluessel: '00012' },
];
