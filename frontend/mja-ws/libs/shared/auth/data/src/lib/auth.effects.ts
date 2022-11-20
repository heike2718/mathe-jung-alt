import { HttpClient } from '@angular/common/http';
import { Inject, inject, Injectable } from '@angular/core';
import { Message } from '@mja-ws/shared/messaging/model';
import { Configuration } from '@mja-ws/shared/config';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { concatMap, map, switchMap, tap } from 'rxjs/operators';
import { authActions } from './auth.actions';
import { createNoopAction } from '@mja-ws/shared/ngrx-utils';
import { Session } from './internal.model';

@Injectable({
    providedIn: 'root'
})
export class AuthEffects {

    #actions = inject(Actions);
    #httpClient = inject(HttpClient);

    constructor(@Inject(Configuration) private configuration: Configuration) { }


    requestLoginUrl$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.requestLoginUrl),
            switchMap(() => this.#httpClient.get<Message>(this.configuration.baseUrl + '/authurls/login/' + this.configuration.clientType)),
            map((message: Message) => authActions.redirectToAuthprovider({ authUrl: message.message }))
        );

    });

    redirectToLogin$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.redirectToAuthprovider),
            concatMap((action) => of(action.authUrl)),
            map((authUrl) => createNoopAction(() => { window.location.href = authUrl }))
        );
    });

    createSession$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(authActions.initSession),
            switchMap(({ authResult }) =>
                this.#httpClient.post<Session>(this.configuration.baseUrl + '/login/' + this.configuration.clientType, authResult)
            ),
            map((session: Session) => authActions.sessionCreated({ session }))
        );
    });
}