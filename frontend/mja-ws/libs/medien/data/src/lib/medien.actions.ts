import { PageDefinition } from '@mja-ws/core/model';
import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { MedienSuchmodus, MediensucheResult, MediensucheTrefferItem, MediumDto } from '@mja-ws/medien/model';

export const medienActions = createActionGroup({
  source: 'medien',
  events: {
    'EDIT_MEDIUM': props<{medium: MediumDto}>(),
    'FIND_MEDIEN': props<{suchmodus: MedienSuchmodus, suchstring: string, pageDefinition: PageDefinition}>(),
    'FIND_MEDIENDETAILS': props<{suchstring: string}>(),
    'MEDIEN_FOUND': props<{result: MediensucheResult}>(),
    'MEDIEN_SELECT_PAGE': props<{pageDefinition: PageDefinition}>(),
    'MEDIEN_SUCHSTRING_CHANGED': props<{suchstring: string}>(),
    'MEDIENDETAILS_FOUND': props<{mediendetails: MediumDto[]}>(),    
    'MEDIUM_SAVED': props<{medium: MediumDto}>(),
    'MEDIUMDETAILS_LOADED': props<{details: MediumDto}>(),
    'SELECT_MEDIUM': props<{id: string}>(),
    'SAVE_MEDIUM': props<{medium: MediumDto}>(),
    'UNSELECT_MEDIUM': emptyProps(),
  }
});