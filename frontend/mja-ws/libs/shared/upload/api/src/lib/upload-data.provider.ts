import { UploadEffects, uploadFeature } from "@mja-ws/shared/upload/data";
import { provideEffects } from "@ngrx/effects";
import { provideState } from "@ngrx/store";


export const uploadDataProvider = [
    provideState(uploadFeature),
    provideEffects(UploadEffects)

];
