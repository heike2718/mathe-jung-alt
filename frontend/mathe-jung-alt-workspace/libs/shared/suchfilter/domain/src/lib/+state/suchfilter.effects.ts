import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import * as SuchfilterActions from './suchfilter.actions';
import { noopAction, SafeNgrxService } from '@mathe-jung-alt-workspace/shared/utils';
import { map } from 'rxjs';
import { DeskriptorDataService } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';

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
