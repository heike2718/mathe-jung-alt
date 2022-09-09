import { PageDefinition } from "@mja-workspace/suchfilter/domain";
import { createAction, props } from "@ngrx/store";


export const selectPage = createAction(
    '[Raetselgruppen] setPageDefinition',
    props<{ pageDefinition: PageDefinition }>()
);
