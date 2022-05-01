import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MessageService } from "@mathe-jung-alt-workspace/shared/ui-messaging";
import { TypedAction } from "@ngrx/store/src/models";
import { catchError, concatMap, exhaustMap, map, mergeMap, Observable, of, OperatorFunction, switchMap, tap } from 'rxjs';
import { ConstraintViolation } from "../http-utils/http.context";


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

        const httpErrorResponse = this.getHttpErrorResponse(error);
        if (!httpErrorResponse) {
            console.log('SafeNgrxService: error=' + JSON.stringify(error));
            this.messageService.error(errorMessage);
        }

        const status = httpErrorResponse?.status;

        switch (status) {
            case 400:
                const cvs = this.extractConstraintViolations(error);
                if (cvs) {
                    this.messageService.error(cvs);
                } else {
                    this.messageService.error(errorMessage);
                }
                break;
            default:
                this.messageService.error(errorMessage);
                break;
        }
    }

    private getHttpErrorResponse(error: any): HttpErrorResponse | undefined {

        if (error instanceof HttpErrorResponse) {
            return <HttpErrorResponse>error;
        }

        return undefined;

    }

    private extractConstraintViolations(error: any): string | undefined {

        if (error instanceof HttpErrorResponse) {
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

                    return message;
                }
            }
        }

        return undefined;


    }
}
