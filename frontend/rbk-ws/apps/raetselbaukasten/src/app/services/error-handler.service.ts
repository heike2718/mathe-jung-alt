import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandler, Injectable, Injector, inject } from '@angular/core';
import { AuthFacade } from '@rbk-ws/core/api';
import { extractServerErrorMessage, getHttpErrorResponse } from '@rbk-ws/shared/http';
import { MessageService } from '@rbk-ws/shared/messaging/api';

@Injectable({
  providedIn: 'root',
})
export class ErrorHandlerService implements ErrorHandler {
  private injector = inject(Injector);

  handleError(error: NonNullable<unknown>): void {
    const messageService = this.injector.get(MessageService);

    const httpErrorResponse: HttpErrorResponse | undefined = getHttpErrorResponse(error);

    if (httpErrorResponse === undefined) {
      this.#handleAnyOtherError(error, messageService);
    } else {
      this.#handleHttpError(httpErrorResponse, messageService);
    }
  }

  #handleHttpError(httpErrorResponse: HttpErrorResponse, messageService: MessageService): void {
    if (httpErrorResponse.status === 440 || httpErrorResponse.status === 401) {
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
