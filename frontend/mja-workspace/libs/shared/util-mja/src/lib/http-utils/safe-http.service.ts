import { Injectable } from "@angular/core";
import { catchError, concatMap, Observable, of } from "rxjs";
import { LoadingIndicatorService } from "../loading-service/loading-indicator.service";
import { MessageService } from "../message-utils/message.service";


@Injectable({
    providedIn: 'root'
})
export class SafeHttpService {

    constructor(private loadingIndicatorService: LoadingIndicatorService, private messageService: MessageService) { }


    public wrapBackendCall<T>(obs$: Observable<T>, errorMessage: string, errorResult: T): Observable<T> {


        return of(this.loadingIndicatorService.showLoaderUntilCompleted(obs$)).pipe(
            concatMap((res) => res),
            catchError(() => {
                this.messageService.error(errorMessage);
                return of(errorResult)
            })
        );
    }

}