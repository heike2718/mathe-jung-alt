import { EmbeddableImageContext, CreateEmbeddableImageResponse, TEXTART, UploadUIModel } from "@mja-ws/shared/upload/model";
import { createActionGroup, emptyProps, props } from "@ngrx/store";


export const uploadActions = createActionGroup({
    source: 'Upload',
    events: {
        'UPLOAD_UI_MODEL_CREATED': props<{uiModel: UploadUIModel}>(),
        'FILE_SELECTED': props<{files: FileList}>(),
        'CREATE_EMBEDDABLE_IMAGE': props<{file: File, context: EmbeddableImageContext}>(),
        'EMBEDDABLE_IMAGE_CREATED': props<{response: CreateEmbeddableImageResponse}>(),
        'UPDATE_FILE': props<{file: File, pfad: string, raetselId: string}>(),
        'UPDATE_FILE_SUCCESS': props<{message: string}>(),
        'UPLOAD_ERROR': props<{errormessage: string}>()
    }
});

