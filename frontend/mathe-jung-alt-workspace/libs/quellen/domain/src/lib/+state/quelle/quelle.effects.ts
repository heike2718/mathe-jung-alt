import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import * as QuelleActions from './quelle.actions';
import { QuelleDataService } from '../../infrastructure/quelle.data.service';

@Injectable()
export class QuelleEffects {
  loadQuelle$ = createEffect(() =>
    this.actions$.pipe(
      ofType(QuelleActions.loadQuelle),
      switchMap((action) =>
        this.quelleDataService.load().pipe(
          map((quelle) => QuelleActions.loadQuelleSuccess({ quelle })),
          catchError((error) => of(QuelleActions.loadQuelleFailure({ error })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private quelleDataService: QuelleDataService
  ) {}
}
