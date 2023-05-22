import { createSelector } from "@ngrx/store";
import { uploadFeature } from './upload.reducer';

const { selectUploadState } = uploadFeature;

const selectedFilesState = createSelector(selectUploadState,
    (state) => state.selectedFiles);

const currentFileState = createSelector(selectUploadState,
    (state) => state.currentFile);

const fileSizeState = createSelector(selectUploadState,
    (state) => state.fileSize);


const maxFileSizeInfoState = createSelector(selectUploadState,
    (state) => state.maxFileSizeInfo);

const maxFilesizeExceededState = createSelector(selectUploadState,
    (state) => state.maxFilesizeExceeded);

const uploadingState = createSelector(selectUploadState,
    (state) => state.uploading);

const uiModelState = createSelector(selectUploadState,
    (state) => state.uiModel);

export const fromUploads = {
    uiModelState,
    selectedFilesState,
    currentFileState,
    fileSizeState,
    maxFileSizeInfoState,
    maxFilesizeExceededState,
    uploadingState
};









