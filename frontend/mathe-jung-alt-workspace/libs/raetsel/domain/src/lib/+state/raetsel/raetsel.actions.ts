import { PageDefinition, Suchfilter, Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { createAction, props } from '@ngrx/store';
import { EditRaetselPayload, GeneratedImages, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, LATEX_OUTPUTFORMAT, Raetsel, RaetselDetails, RaetselDetailsContent } from '../../entities/raetsel';

export const prepareSearch = createAction(
  '[Raetsel] prepareSearch',
  props<{suchfilter: Suchfilter, pageDefinition: PageDefinition}>()
);

export const selectPage = createAction(
  '[Raetsel] setPageDefinition',
  props<{ pageDefinition: PageDefinition }>()
);

export const countRaetsel = createAction(
  '[Raetsel] count'
);

export const raetselCounted = createAction(
  '[Raetsel] count completed',
  props<{ anzahl: number }>()
);

export const findRaetsel = createAction(
  '[Raetsel] find Raetsel',
  props<{suchfilter: Suchfilter, pageDefinition: PageDefinition, kontext: Suchkontext}>()
);

export const findRaetselSuccess = createAction(
  '[Raetsel] find Raetsel Success',
  props<{ raetsel: Raetsel[] }>()
);

export const pageSelected = createAction(
  '[Raetsel] page selected',
  props<{ raetsel: Raetsel[] }>()
);

export const raetsellisteCleared = createAction(
  '[Raetsel] raetselliste cleared ');

export const raetselSelected = createAction(
  '[Raetsel] raetselliste raetsel selected',
  props<{ raetsel: Raetsel }>()
);

export const raetselDetailsLoaded = createAction(
  '[Raetsel] details loaded',
  props<{ raetselDetails: RaetselDetails }>()
);

export const showRaetselDetails = createAction(
  '[Raetsel] show details'
);

export const editRaetsel = createAction(
  '[Raetsel]  edit',
  props<{ raetselDetailsContent: RaetselDetailsContent }>()
);

export const generateOutput = createAction(
  '[Raetsel] generate output',
  props<{ raetselId: string, outputFormat: LATEX_OUTPUTFORMAT, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE }>()
);

export const outputGenerated = createAction(
  '[Raetsel] output generated',
  props<{ images: GeneratedImages }>()
);

export const generateOutputError = createAction(
  '[Raetsel] generate error'
);

export const cancelEdit = createAction(
  '[Raetsel] cancel edit'
);

export const startSaveRaetsel = createAction(
  '[Raetsel] start save',
  props<{ editRaetselPayload: EditRaetselPayload }>()
);

export const saveRaetsel = createAction(
  '[Raetsel] save raetsel',
  props<{ editRaetselPayload: EditRaetselPayload, csrfToken: string | null }>()
);

export const raetselSaved = createAction(
  '[Raetsel] raetsel saved',
  props<{ raetselDetails: RaetselDetails, successMessage: string }>()
);



