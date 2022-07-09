import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { map, switchMap } from 'rxjs/operators';
import * as QuelleActions from './quelle.actions';
import { QuelleDataService } from '../../infrastructure/quelle.data.service';
import { QuellenFacade } from '../../application/quellen.facade';
import { SafeNgrxService, noopAction } from '@mja-workspace/shared/util-mja';
import * as AuthActions from '@mja-workspace/shared/auth/domain';
import { Quelle } from '../../entities/quelle';

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
      switchMap((action) => this.quellenFacade.quellenList$.pipe(
        map((quellen) => QuelleActions.pageSelected({ quellen: quellen.slice(action.pageIndex * action.pageSize, (action.pageIndex + 1) * action.pageSize) }))
      ))
    )
  );

  loadQuelleAfterAdminLoggedIn$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.sessionCreated),
      this.safeNgrx.safeSwitchMap((action) =>
        this.quelleDataService.loadQuelleAdmin(action.session.user).pipe(
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
      this.safeNgrx.safeSwitchMap((action) =>
        this.quelleDataService.loadQuelleAdmin(action.session.user).pipe(
          map((quelle: Quelle | undefined) =>
            quelle ? QuelleActions.quelleFound({ quelle }) : noopAction()
          )
        ), 'Ups, beim Laden der Quelle ist etwas schiefgegangen', noopAction()
      )
    )
  );

  constructor(
    private actions$: Actions,
    private quellenFacade: QuellenFacade,
    private quelleDataService: QuelleDataService,
    private safeNgrx: SafeNgrxService
  ) { }
}
