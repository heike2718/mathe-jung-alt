import { createFeature, createReducer, on } from "@ngrx/store";
import { UploadUIModel, initialUploadUIModel } from '@mja-ws/shared/upload/model';
import { uploadActions } from "./upload.actions";


export interface UploadState {
    readonly uiModel: UploadUIModel;
    readonly selectedFiles: FileList | undefined;
    readonly currentFile: File | undefined;
    readonly uploading: boolean;
    readonly fileSize: string;
    readonly maxFilesizeExceeded: boolean;
    readonly maxFileSizeInfo: string;
};

const initialUploadState: UploadState = {
    uiModel: initialUploadUIModel,
    selectedFiles: undefined,
    currentFile: undefined,
    uploading: false,
    fileSize: '',
    maxFilesizeExceeded: false,
    maxFileSizeInfo: ''
};

export const uploadFeature = createFeature({
    name: 'upload',
    reducer: createReducer(
        initialUploadState,
        on(uploadActions.upload_ui_model_created, (state, action) => {

            const maxFileSizeInKB = action.uiModel.maxSizeBytes / 1024;
            const maxFileSizeInMB = maxFileSizeInKB / 1024;
            const maxFileSizeInfo = 'Maximale erlaubte Größe: ' + maxFileSizeInKB + ' kB bzw. ' + maxFileSizeInMB + ' MB';

            return { ...state, maxFileSizeInfo: maxFileSizeInfo };
        }),
        on(uploadActions.file_selected, (state, action) => {

            const fileList: FileList = action.files;
            const size = fileList[0].size;
            const fileSize = calculateFileSize(size);

            if (size > state.uiModel.maxSizeBytes) {
                return {...state, maxFilesizeExceeded: true, maxFileSizeInfo: 'Die Datei ist zu groß: ' + fileSize + '. ' +  state.maxFileSizeInfo}
            }

            return { ...state, selectedFiles: action.files };
        }),
        on(uploadActions.upload_file, (state, _action) => {

            return {...state, uploading: true};
        }),
        on(uploadActions.upload_success, (state, _action) => {

            return {...state, uploading: false, selectedFiles: undefined, currentFile: undefined};
        }),
        on(uploadActions.upload_error, (state, _action, ) => {

            return {...state, uploading: false, selectedFiles: undefined, currentFile: undefined};
        })
    )
});


function calculateFileSize(size: number): string {

    // let kb = size / 1024;

    // console.log('kb: ' + kb);

    if (Math.round(size / 1024) < 2048) {
        return Math.round(size / 1024) + ' kB';
    } else {
        return Math.round(size / 1024 / 1024) + ' MB';
    }
};



