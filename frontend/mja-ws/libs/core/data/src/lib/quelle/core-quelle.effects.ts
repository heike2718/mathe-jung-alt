import { HttpClient, HttpContext } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { QuelleUI } from '@mja-ws/core/model';
import { SILENT_LOAD_CONTEXT } from '@mja-ws/shared/messaging/api';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { concatMap, map } from 'rxjs';
import { coreQuelleActions } from './core-quelle.actions';

@Injectable({
    providedIn: 'root'
})
export class CoreQuelleEffects {

    #actions = inject(Actions);
    #httpClient = inject(HttpClient);

    loadQuelleAdmin$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(coreQuelleActions.lOAD_QUELLE_ADMIN),
            concatMap(() =>
                this.#httpClient.get<QuelleUI>('/mja-api/quellen/admin/v2', {context: new HttpContext().set(SILENT_LOAD_CONTEXT, true)})
            ),
            map((quelle: QuelleUI) => coreQuelleActions.cORE_QUELLE_ADMIN_LOADED({ quelle }))
        );
    });
}
