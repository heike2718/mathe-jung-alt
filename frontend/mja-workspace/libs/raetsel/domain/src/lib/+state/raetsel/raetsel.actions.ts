import { GeneratedImages, GeneratedPDF, LATEX_LAYOUT_ANTWORTVORSCHLAEGE } from '@mja-workspace/shared/ui-components';
import { SelectableItem } from '@mja-workspace/shared/util-mja';
import { PageDefinition, Suchfilter, Suchkontext } from '@mja-workspace/suchfilter/domain';
import { createAction, props } from '@ngrx/store';
import { Raetsel, RaetselDetails, RaetselDetailsContent, EditRaetselPayload, RaetselsucheTreffer } from '../../entities/raetsel';

export const setSuchfilter = createAction(
  '[Raetsel] setSuchfilter',
  props<{ suchfilter: Suchfilter}>()
)

export const selectPage = createAction(
  '[Raetsel] setPageDefinition',
  props<{ pageDefinition: PageDefinition }>()
);

export const findRaetsel = createAction(
  '[Raetsel] find Raetsel',
  props<{ suchfilter: Suchfilter, pageDefinition: PageDefinition, kontext: Suchkontext }>()
);

export const findRaetselSuccess = createAction(
  '[Raetsel] find Raetsel Success',
  props<{ suchergebnis: RaetselsucheTreffer }>()
);

export const pageSelected = createAction(
  '[Raetsel] page selected',
  props<{ raetsel: Raetsel[] }>()
);

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

export const raetsellisteCleared = createAction(
  '[Raetsel] raetselliste cleared '
);

export const raetselDeskriptorenLoaded = createAction(
  '[Raetsel] raetselDeskriptorenLoaded',
  props<{selectableDeskriptoren: SelectableItem[]}>()
);

export const editRaetsel = createAction(
  '[Raetsel]  edit',
  props<{ raetselDetailsContent: RaetselDetailsContent }>()
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
  props<{ raetselDetails: RaetselDetails, successMessage: string, insert: boolean }>()
);

export const generateRaetselPNGs = createAction(
  '[Raetsel] generate PNGs',
  props<{ raetselID: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE }>()
);

export const raetselPNGsGenerated = createAction(
  '[Raetsel] raetsel PNGs generated',
  props<{ images: GeneratedImages }>()
);

export const generateRaetselPDF = createAction(
  '[Raetsel] generate PDF',
  props<{ raetselId: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE }>()
);

export const raetselPDFGenerated = createAction(
  '[Raetsel] raetsel PDF generated',
  props<{ pdf: GeneratedPDF }>()
);

export const generateOutputError = createAction(
  '[Raetsel] generate error'
);

