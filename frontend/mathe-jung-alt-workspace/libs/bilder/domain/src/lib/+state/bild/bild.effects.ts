import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import * as BildActions from './bild.actions';
import { BildDataService } from '../../infrastructure/bild.data.service';

@Injectable()
export class BildEffects {
  loadBild$ = createEffect(() =>
    this.actions$.pipe(
      ofType(BildActions.loadBild),
      switchMap((action) =>
        this.bildDataService.load().pipe(
          map((bild) => BildActions.loadBildSuccess({ bild })),
          catchError((error) => of(BildActions.loadBildFailure({ error })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private bildDataService: BildDataService
  ) {}
}
