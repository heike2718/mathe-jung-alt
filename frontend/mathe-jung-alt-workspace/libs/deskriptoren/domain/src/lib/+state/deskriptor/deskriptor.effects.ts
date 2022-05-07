import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import * as DeskriptorActions from './deskriptor.actions';
import { DeskriptorDataService } from '../../infrastructure/deskriptor.data.service';
import { noopAction, SafeNgrxService } from '@mathe-jung-alt-workspace/shared/utils';
import { map } from 'rxjs';

@Injectable()
export class DeskriptorEffects {

  loadDeskriptor$ = createEffect(() =>
    this.actions$.pipe(
      ofType(DeskriptorActions.loadDeskriptoren),
      this.safeNgrx.safeSwitchMap((action) =>
        this.deskriptorDataService.load(action.kontext).pipe(
          map((deskriptor) =>
            DeskriptorActions.loadDeskriptorenSuccess({ deskriptor })
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
