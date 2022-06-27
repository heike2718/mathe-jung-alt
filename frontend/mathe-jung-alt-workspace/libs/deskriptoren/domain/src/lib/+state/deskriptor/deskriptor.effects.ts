import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import * as DeskriptorActions from './deskriptor.actions';
import { noopAction, SafeNgrxService } from '@mathe-jung-alt-workspace/shared/utils';
import { map } from 'rxjs';
import { DeskriptorDataService } from '../../infrastructure/deskriptor.data.service';

@Injectable()
export class DeskriptorEffects {

  loadDeskriptoren$ = createEffect(() =>
    this.actions$.pipe(
      ofType(DeskriptorActions.loadDeskriptoren),
      this.safeNgrx.safeSwitchMap((_action) =>
        this.deskriptorDataService.load().pipe(
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
