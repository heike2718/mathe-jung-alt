import { Injectable } from "@angular/core";
import { MessageService } from "@mathe-jung-alt-workspace/shared/ui-messaging";
import { TypedAction } from "@ngrx/store/src/models";
import { catchError, concatMap, Observable, of, OperatorFunction, switchMap } from 'rxjs';


@Injectable({
    providedIn: 'root'
})
export class SafeNgrxService {

    constructor(private messageService: MessageService) { }

    public safeConcatMap<S, T extends string>(
        project: (value: S) => Observable<TypedAction<T>>,
        errorMessage: string,
        errorAction: TypedAction<T>
    ): OperatorFunction<S, TypedAction<T | 'NOOP'>> {
        return (source$: Observable<S>): Observable<TypedAction<T | 'NOOP'>> =>
            source$.pipe(
                concatMap((value) =>
                    project(value).pipe(catchError(() => {
                        this.messageService.error(errorMessage);
                        return of(errorAction);
                    }))
                )
            );
    }

    public safeSwitchMap<S, T extends string>(
        project: (value: S) => Observable<TypedAction<T>>,
        errorMessage: string,
        errorAction: TypedAction<T>
    ): OperatorFunction<S, TypedAction<T | 'NOOP'>> {
        return (source$: Observable<S>): Observable<TypedAction<T | 'NOOP'>> =>
            source$.pipe(
                switchMap((value) =>
                    project(value).pipe(catchError((error) => {
                        console.log('SafeNgrxService: error=' + error);
                        this.messageService.error(errorMessage);
                        return of(errorAction);
                    }))
                )
            );
    }
}
