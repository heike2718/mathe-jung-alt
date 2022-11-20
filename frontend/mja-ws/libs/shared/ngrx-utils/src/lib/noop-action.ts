import { Action, createAction } from '@ngrx/store';

export function createNoopAction(fn: Function): Action {
    fn();
    return createAction('[Util] NOOP');
};
