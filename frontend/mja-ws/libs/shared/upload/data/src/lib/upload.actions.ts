import { UploadUIModel } from "@mja-ws/shared/upload/model";
import { createActionGroup, props } from "@ngrx/store";


export const uploadActions = createActionGroup({
    source: 'Upload',
    events: {
        'UPLOAD_UI_MODEL_CREATED': props<{uiModel: UploadUIModel}>(),
        'FILE_SELECTED': props<{files: FileList}>(),
        'UPDATE_FILE': props<{file: File, pfad: string, raetselId: string}>(),
        'UPDATE_FILE_SUCCESS': props<{message: string}>(),
        'UPLOAD_ERROR': props<{errormessage: string}>()
    }
});

