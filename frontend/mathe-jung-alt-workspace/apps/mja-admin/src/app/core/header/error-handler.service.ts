import { ErrorHandler, Injectable, Injector } from "@angular/core";
import { MessageService } from "@mathe-jung-alt-workspace/shared/ui-messaging";

@Injectable()
export class ErrorHandlerService implements ErrorHandler {

    constructor(private injector: Injector) { }

    handleError(error: any): void {

        const messageService = this.injector.get(MessageService);
        messageService.error('UI, da ist eine unerwarteter Fehler passiert');
        console.log(error);
    }
}