import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { UploadHttpService } from "./upload-http.service";
import { uploadActions } from "./upload.actions";
import { catchError, map, of, switchMap, tap } from "rxjs";
import { Message, MessageService } from "@mja-ws/shared/messaging/api";

@Injectable({
    providedIn: 'root'
})
export class UploadEffects {

    #actions = inject(Actions);
    #uploadService = inject(UploadHttpService);
    #messageService = inject(MessageService);

    uploadFile$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(uploadActions.upload_file),
            switchMap((action) => this.#uploadService.uploadFile(action.file, action.pfad)),
            map((messagePayload: Message) => uploadActions.upload_success({ message: messagePayload.message })),
            catchError(() => of(uploadActions.upload_error({ errormessage: 'Beim Hochladen der Datei ist ein Fehler aufgetreten. Details stehen im server.log' })))
        )
    });

    uploadSuccess$ = createEffect(() =>

        this.#actions.pipe(
            ofType(uploadActions.upload_success),
            tap((action) => {
                this.#messageService.info(action.message);
            }),
        ), { dispatch: false });

    uploadError$ = createEffect(() =>

        this.#actions.pipe(
            ofType(uploadActions.upload_error),
            tap((action) => {
                this.#messageService.error(action.errormessage);
            }),
        ), { dispatch: false });


}