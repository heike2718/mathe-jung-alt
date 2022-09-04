import { HttpErrorResponse } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Configuration, SharedConfigService, STORAGE_KEY_QUELLE, STORAGE_KEY_SESSION } from '@mja-workspace/shared/util-configuration';
import { TypedAction } from '@ngrx/store/src/models';
import { catchError, concatMap, exhaustMap, mergeMap, Observable, of, OperatorFunction, switchMap, tap } from 'rxjs';
import { ConstraintViolation } from '../http-utils/http.context';
import { Message } from '../message-utils/message';
import { MessageService } from '../message-utils/message.service';


@Injectable({
    providedIn: 'root'
})
export class SafeNgrxService {

    #storagePrefix!: string;

    constructor(private messageService: MessageService,
        @Inject(SharedConfigService) private configuration: Configuration,
        private router: Router

    ) {
        this.#storagePrefix = this.configuration.storagePrefix;
    }

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

    clearSession(): void {
        localStorage.removeItem(this.#storagePrefix + STORAGE_KEY_SESSION);
        localStorage.removeItem(STORAGE_KEY_QUELLE);
    }

    handleError(error: any, errorMessage: string): void {

        window.location.hash = '';

        const httpErrorResponse: HttpErrorResponse | undefined = this.#getHttpErrorResponse(error);
        if (httpErrorResponse === undefined) {
            if (errorMessage.length > 0) {
                this.messageService.error(errorMessage);
            }
        } else {
            const status = httpErrorResponse.status;
            if (status === 440) {
                this.clearSession();
                this.router.navigateByUrl('home')
                this.messageService.warn('Die Session ist abgelaufen. Bitte neu einloggen.');
            } else {
                const message = this.#extractServerErrorMessage(httpErrorResponse);
                if (message) {
                    if (message.level === 'WARN') {
                        this.messageService.warn(message.message);
                    } else {
                        this.messageService.error(message.message);
                    }
                } else {
                    if (errorMessage.length > 0) {
                        this.messageService.error(errorMessage);
                    }
                }
            }
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    #getHttpErrorResponse(error: any): HttpErrorResponse | undefined {

        if (error instanceof HttpErrorResponse) {
            return <HttpErrorResponse>error;
        }

        return undefined;

    }



    #extractServerErrorMessage(error: HttpErrorResponse): Message | undefined {

        if (error.status === 0) {
            this.clearSession();
            this.router.navigateByUrl('home');
            window.location.reload();
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
            } else {
                const payload: Message = errorResponse.error;

                if (payload) {
                    return payload;
                }
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
