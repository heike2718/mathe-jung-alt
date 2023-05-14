import { UploadUIModel } from "@mja-ws/shared/upload/model";
import { createActionGroup, emptyProps, props } from "@ngrx/store";


export const uploadActions = createActionGroup({
    source: 'Upload',
    events: {
        'UPLOAD_UI_MODEL_CREATED': props<{uiModel: UploadUIModel}>(),
        'FILE_SELECTED': props<{files: FileList}>(),
        'UPLOAD_FILE': props<{file: File, pfad: string}>()
    }
});

