import { HttpClient, HttpContext } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { DeskriptorUI } from '@mja-ws/core/model';
import { SILENT_LOAD_CONTEXT } from '@mja-ws/shared/messaging/api';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { concatMap, map } from 'rxjs';
import { coreDeskriptorenActions } from './core-deskriptoren.actions';

@Injectable({
    providedIn: 'root'
})
export class CoreDeskriptorUIEffects {

    #actions = inject(Actions);
    #httpClient = inject(HttpClient);

    loadDeskriptoren$ = createEffect(() => {

        return this.#actions.pipe(
            ofType(coreDeskriptorenActions.lOAD_DESKRIPTOREN),
            concatMap(() =>
                this.#httpClient.get<DeskriptorUI[]>('/mja-api/deskriptoren/v2', {context: new HttpContext().set(SILENT_LOAD_CONTEXT, true)})
            ),
            map((deskriptoren: DeskriptorUI[]) => coreDeskriptorenActions.cORE_DESKRIPTOREN_LOADED({ deskriptoren }))
        )
    });

}