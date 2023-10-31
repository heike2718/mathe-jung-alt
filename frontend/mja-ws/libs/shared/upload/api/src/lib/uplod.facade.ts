import { Injectable, inject } from '@angular/core';
import { UploadUIModel, initialUploadUIModel } from "@mja-ws/shared/upload/model";
import { uploadActions} from '@mja-ws/shared/upload/data';
import { Store } from '@ngrx/store';
import { EmbeddableImageContext } from 'libs/embeddable-images/model/src/embeddable-images.model';

@Injectable({
    providedIn: 'root'
})
export class UploadFacade {

    #store = inject(Store);


    initUploadUIModel(title: string, pfad: string, acceptFileType: string): void {

        const acceptFileTypeMessage = 'erlaubte Dateitypen: ' + acceptFileType;        

        const uiModel: UploadUIModel = {
            ...initialUploadUIModel,
            pfad: pfad,
            titel: title,
            acceptFileType: '.' + acceptFileType,
            acceptMessage: acceptFileTypeMessage
        };

        this.#store.dispatch(uploadActions.uPLOAD_UI_MODEL_CREATED({uiModel: uiModel}));
    }

    uploadFile(file: File, uiModel: UploadUIModel): void {

        this.#store.dispatch(uploadActions.uPDATE_FILE({file: file, pfad: uiModel.pfad, raetselId: uiModel.context.raetselId}));
    }

}