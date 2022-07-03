import { Injectable } from "@angular/core";
import { SafeNgrxService, noopAction } from "@mja-workspace/shared/util-mja";
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { map } from "rxjs";
import { DeskriptorDataService } from "../infrastructure/deskriptor-data.service";
import * as SuchfilterActions from './suchfilter.actions';


@Injectable()
export class SuchfilterEffects {


    loadDeskriptoren$ = createEffect(() =>
        this.actions$.pipe(
            ofType(SuchfilterActions.loadDeskriptoren),
            this.safeNgrx.safeSwitchMap((_action) =>
                this.deskriptorDataService.load().pipe(
                    map((deskriptoren) =>
                        SuchfilterActions.loadDeskriptorenSuccess({ deskriptoren })
                    )
                ), 'Ups, beim Laden der Deskriptoren ist etwas schiefgegangen', noopAction()
            )

        )
    );


    constructor(
        private actions$: Actions,
        private deskriptorDataService: DeskriptorDataService,
        private safeNgrx: SafeNgrxService
    ) { }

}