import { ErrorHandler, Injectable, Injector } from "@angular/core";
import { MessageService } from "@mathe-jung-alt-workspace/shared/util-mja";


@Injectable({
    providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

    constructor(private injector: Injector) {}

    handleError(error: any): void {
       
        const messageService = this.injector.get(MessageService);
        messageService.error('Upsi, da ist ein unerwarteter Fehler aufgetreten');
        console.error(error);
    }
}
