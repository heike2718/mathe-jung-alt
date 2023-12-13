import { HttpErrorResponse } from "@angular/common/http";
import { ErrorHandler, Injectable, Injector } from "@angular/core";
import { AuthFacade } from "@mja-ws/shared/auth/api";
import { extractServerErrorMessage, getHttpErrorResponse } from "@mja-ws/shared/http";
import { MessageService } from "@mja-ws/shared/messaging/api";


@Injectable({
    providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

    constructor(private injector: Injector) { }

    handleError(error: NonNullable<unknown>): void {

        const messageService = this.injector.get(MessageService);

        const httpErrorResponse: HttpErrorResponse | undefined = getHttpErrorResponse(error);

        if (httpErrorResponse === undefined) {
            this.#handleAnyOtherError(error, messageService)
        } else {
            this.#handleHttpError(httpErrorResponse, messageService);
        }
    }

    #handleHttpError(httpErrorResponse: HttpErrorResponse, messageService: MessageService): void {

        if (httpErrorResponse.status === 440) {
            this.injector.get(AuthFacade).handleSessionExpired();
        } else {
            const message = extractServerErrorMessage(httpErrorResponse);
            if (message.level === 'WARN') {
                messageService.warn(message.message);
            } else {
                messageService.error(message.message);
            }
        }        
    }

    #handleAnyOtherError(error: unknown, messageService: MessageService): void {
        messageService.error('Upsi, da ist ein unerwarteter Fehler aufgetreten');
        // TODO: müssen das noch an die API senden
        console.error(error);
    }
}
