import { createReducer, on, Action } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter, Update } from '@ngrx/entity';
import * as RaetselgruppenActions from './raetselgruppen.actions';

export const RAETSELGRUPPEN_FEATURE_KEY = 'raetselgruppen';

import { RaetselgruppensucheTrefferItem } from "../entities/raetselgruppen";
import { initialPaginationState, PaginationState } from '@mja-workspace/suchfilter/domain';


export interface RaetselgruppenState extends EntityState<RaetselgruppensucheTrefferItem> {
    readonly page: RaetselgruppensucheTrefferItem[];
    readonly paginationState: PaginationState;
};

export interface RaetselgruppenPartialState {
    readonly [RAETSELGRUPPEN_FEATURE_KEY]: RaetselgruppenState;
};

export const raetselgruppenAdapter: EntityAdapter<RaetselgruppensucheTrefferItem> =
    createEntityAdapter<RaetselgruppensucheTrefferItem>();

const initialState: RaetselgruppenState = raetselgruppenAdapter.getInitialState({
    page: [],
    paginationState: initialPaginationState
});

const raetselgruppenReducer = createReducer(
    initialState,

    on(RaetselgruppenActions.selectPage, (state, action) => {
        return {
            ...state,
            paginationState: {
                ...state.paginationState,
                pageIndex: action.pageDefinition.pageIndex,
                pageSize: action.pageDefinition.pageSize,
                sortDirection: action.pageDefinition.sortDirection
            }
        }
    }),
);

export function reducer(state: RaetselgruppenState | undefined, action: Action) {
    return raetselgruppenReducer(state, action);
};


