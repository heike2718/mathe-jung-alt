import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { filter, map, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import * as QuelleActions from './quelle.actions';
import { QuelleDataService } from '../../infrastructure/quelle.data.service';
import { noopAction, SafeNgrxService } from '@mathe-jung-alt-workspace/shared/utils';
import { QuellenFacade } from '../../application/quellen.facade';
import * as AuthActions from '@mathe-jung-alt-workspace/shared/auth/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { Quelle } from '@mathe-jung-alt-workspace/quellen/domain';

@Injectable()
export class QuelleEffects {

  findQuellen$ = createEffect(() =>
    this.actions$.pipe(
      ofType(QuelleActions.findQuellen),
      this.safeNgrx.safeSwitchMap((action) =>
        this.quelleDataService.findQuellen(action.suchfilter).pipe(
          map((quellen) =>
            QuelleActions.quellenFound({ quellen })
          )
        ), 'Ups, beim Suchen nach Quellen ist etwas schiefgegangen', noopAction()
      )
    )
  );

  loadQuelle$ = createEffect(() =>
    this.actions$.pipe(
      ofType(QuelleActions.findQuelle),
      this.safeNgrx.safeSwitchMap((action) =>
        this.quelleDataService.loadQuelle(action.uuid).pipe(
          map((quelle) =>
            QuelleActions.quelleFound({ quelle })
          )
        ), 'Ups, beim Laden der Quelle ist etwas schiefgegangen', noopAction()
      )
    )
  );

  selectPage$ = createEffect(() =>
    this.actions$.pipe(
      ofType(QuelleActions.selectPage),
      // switchMap, damit das Pagen einer früheren Page-Auswahl gecanceled wird, sobald eine neue Page ausgewählt wurde
      switchMap((action) =>
        this.quellenFacade.quellenList$.pipe(
          map((quellen) => QuelleActions.pageSelected({
            quellen: quellen.slice(action.pageIndex * action.pageSize, (action.pageIndex + 1) * action.pageSize)
          }))
        )
      )
    ));

  loadQuelleAfterAdminLoggedIn$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.sessionCreated),
      withLatestFrom(this.authFacade.getUser$),
      this.safeNgrx.safeSwitchMap(([_action, user]) =>
        this.quelleDataService.loadQuelleAdmin(user).pipe(
          map((quelle: Quelle | undefined) =>
            quelle ? QuelleActions.quelleFound({ quelle }) : noopAction()
          )
        ), 'Ups, beim Laden der Quelle ist etwas schiefgegangen', noopAction()
      )
    )
  );

  loadQuelleSessionRestored$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.sessionRestored),
      withLatestFrom(this.authFacade.getUser$),
      this.safeNgrx.safeSwitchMap(([_action, user]) =>
        this.quelleDataService.loadQuelleAdmin(user).pipe(
          map((quelle: Quelle | undefined) =>
            quelle ? QuelleActions.quelleFound({ quelle }) : noopAction()
          )
        ), 'Ups, beim Laden der Quelle ist etwas schiefgegangen', noopAction()
      )
    )
  );

  constructor(
    private actions$: Actions,
    private authFacade: AuthFacade,
    private quellenFacade: QuellenFacade,
    private quelleDataService: QuelleDataService,
    private safeNgrx: SafeNgrxService
  ) { }


}
