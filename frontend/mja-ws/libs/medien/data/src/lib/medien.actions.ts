import { PageDefinition } from '@mja-ws/core/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { MediensucheResult, MediensucheTrefferItem, MediumDto } from '@mja-ws/medien/model';

export const medienActions = createActionGroup({
  source: 'medien',
  events: {
    'EDIT_MEDIUM': props<{medium: MediumDto, nextUrl: string}>(),
    'FIND_MEDIEN': props<{suchstring: string, pageDefinition: PageDefinition}>(),
    'FIND_MEDIENDETAILS': props<{suchstring: string}>(),
    'MEDIEN_FOUND': props<{result: MediensucheResult}>(),
    'MEDIEN_SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
    'MEDIEN_SUCHSTRING_CHANGED': props<{suchstring: string}>(),
    'MEDIUM_SAVED': props<{medium: MediumDto}>(),
    'MEDIUMDETAILS_LOADED': props<{details: MediumDto, nextUrl: string}>(),
    'SELECT_MEDIUM': props<{medium: MediensucheTrefferItem}>(),
    'SAVE_MEDIUM': props<{medium: MediumDto}>(),
    'UNSELECT_MEDIUM': emptyProps()
  }
});