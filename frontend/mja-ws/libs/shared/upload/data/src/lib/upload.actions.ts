import { Message } from "@mja-ws/shared/messaging/api";
import { UploadUIModel } from "@mja-ws/shared/upload/model";
import { createActionGroup, emptyProps, props } from "@ngrx/store";


export const uploadActions = createActionGroup({
    source: 'Upload',
    events: {
        'UPLOAD_UI_MODEL_CREATED': props<{uiModel: UploadUIModel}>(),
        'FILE_SELECTED': props<{files: FileList}>(),
        'UPLOAD_FILE': props<{file: File, pfad: string, schluessel: string}>(),
        'UPLOAD_SUCCESS': props<{message: string}>(),
        'UPLOAD_ERROR': props<{errormessage: string}>()
    }
});

