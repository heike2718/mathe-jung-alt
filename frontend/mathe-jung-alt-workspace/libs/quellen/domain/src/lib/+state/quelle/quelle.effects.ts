import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { map } from 'rxjs/operators';
import * as QuelleActions from './quelle.actions';
import { QuelleDataService } from '../../infrastructure/quelle.data.service';
import { noopAction, SafeNgrxService } from '@mathe-jung-alt-workspace/shared/utils';

@Injectable()
export class QuelleEffects {
  loadQuelle$ = createEffect(() =>
    this.actions$.pipe(
      ofType(QuelleActions.findQuelle),
      this.safeNgrx.safeSwitchMap((action) =>
        this.quelleDataService.findQuellen(action.suchfilter).pipe(
          map((quellen) =>
            QuelleActions.quellenFound({ quellen })
          )
        ), 'Ups, beim Suchen nach Quellen ist etwas schiefgegangen', noopAction()
      )
    )
  );

  constructor(
    private actions$: Actions,
    private quelleDataService: QuelleDataService,
    private safeNgrx: SafeNgrxService
  ) {}
}
