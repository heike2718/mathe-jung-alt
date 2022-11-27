import { Action, createAction } from '@ngrx/store';

export function createNoopAction(): Action {  
    return createAction('[Util] NOOP');
};
