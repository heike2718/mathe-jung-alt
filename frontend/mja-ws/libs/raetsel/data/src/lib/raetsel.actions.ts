import { createActionGroup, emptyProps } from '@ngrx/store';

export const raetselActions = createActionGroup({
    source: 'Raetsel',
    events: {
        'RAETSELLISTE_CLEARED': emptyProps(),
    }
});
