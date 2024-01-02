import { HttpClient, HttpContext } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { QuelleDto } from '@mja-ws/core/model';
import { SILENT_LOAD_CONTEXT } from '@mja-ws/shared/messaging/api';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { switchMap, map } from 'rxjs';
import { coreQuelleActions } from './core-autor.actions';

@Injectable({
    providedIn: 'root'
})
export class CoreAutorEffects {

    #actions = inject(Actions);
    #httpClient = inject(HttpClient);


    loadQuelleAdmin$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(coreQuelleActions.lOAD_AUTOR),
            switchMap(() =>
                this.#httpClient.get<QuelleDto>('/mja-api/quellen/autor/v2', {context: new HttpContext().set(SILENT_LOAD_CONTEXT, true)})
            ),
            map((quelle: QuelleDto) => coreQuelleActions.cORE_AUTOR_LOADED({ quelle }))
        );
    });
}
