import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { TypedAction } from "@ngrx/store/src/models";
import { catchError, concatMap, exhaustMap, mergeMap, Observable, of, OperatorFunction, switchMap, tap } from 'rxjs';
import { ConstraintViolation } from "../http-utils/http.context";
import { Message } from "../message-utils/message";
import { MessageService } from "../message-utils/message.service";


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
                    project(value).pipe(catchError((error) => {
                        this.handleError(error, errorMessage);
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
                        this.handleError(error, errorMessage);
                        return of(errorAction);
                    }))
                )
            );
    }

    public safeExhaustMap<S, T extends string>(
        project: (value: S) => Observable<TypedAction<T>>,
        errorMessage: string,
        errorAction: TypedAction<T>
    ): OperatorFunction<S, TypedAction<T | 'NOOP'>> {
        return (source$: Observable<S>): Observable<TypedAction<T | 'NOOP'>> =>
            source$.pipe(
                exhaustMap((value) =>
                    project(value).pipe(catchError((error) => {
                        this.handleError(error, errorMessage);
                        return of(errorAction);
                    }))
                )
            );
    }

    public safeMergeMap<S, T extends string>(
        project: (value: S) => Observable<TypedAction<T>>,
        errorMessage: string,
        errorAction: TypedAction<T>
    ): OperatorFunction<S, TypedAction<T | 'NOOP'>> {
        return (source$: Observable<S>): Observable<TypedAction<T | 'NOOP'>> =>
            source$.pipe(
                mergeMap((value) =>
                    project(value).pipe(catchError((error) => {
                        this.handleError(error, errorMessage);
                        return of(errorAction);
                    }))
                )
            );
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private handleError(error: any, errorMessage: string): void {

        window.location.hash = '';

        const httpErrorResponse: HttpErrorResponse | undefined = this.getHttpErrorResponse(error);
        if (httpErrorResponse === undefined) {
            console.log('SafeNgrxService: error=' + error);
            this.messageService.error(errorMessage);
            // this.messageService.error(error);
        } else {
            const message = this.extractServerErrorMessage(httpErrorResponse);
            if (message) {
                if (message.level === 'WARN') {
                    this.messageService.warn(message.message);
                } else {
                    this.messageService.error(message.message);
                }
            } else {
                this.messageService.error(errorMessage);
            }
        }
    }

    private getHttpErrorResponse(error: any): HttpErrorResponse | undefined {

        if (error instanceof HttpErrorResponse) {
            return <HttpErrorResponse>error;
        }

        return undefined;

    }



    private extractServerErrorMessage(error: HttpErrorResponse): Message | undefined {

        if (error.status === 0) {
            return { level: 'ERROR', message: 'Der Server ist nicht erreichbar.' };
        }

        const errorResponse: HttpErrorResponse = <HttpErrorResponse>error;

        if (errorResponse.status === 400) {
            const error = errorResponse.error;

            if (error['violations']) {

                const violations: ConstraintViolation[] = <ConstraintViolation[]>error['violations'];

                let message = '';
                violations.forEach(v => {
                    const index = v.field.indexOf('.');
                    const field = v.field.substring(index + 1);
                    message += field + ': ' + v.message;
                });

                return { level: 'ERROR', message: message };
            }
        } else {
            const payload: Message = errorResponse.error;

            if (payload) {
                return payload;
            }
        }

        return undefined;
    }
}
