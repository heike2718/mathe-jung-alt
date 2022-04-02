import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import * as RaetselActions from './raetsel.actions';
import { RaetselDataService } from '../../infrastructure/raetsel.data.service';

@Injectable()
export class RaetselEffects {
  
  loadRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.loadRaetsel),
      switchMap((action) =>
        this.raetselDataService.load().pipe(
          map((raetsel) => RaetselActions.loadRaetselSuccess({ raetsel })),
          catchError((error) =>
            of(RaetselActions.loadRaetselFailure({ error }))
          )
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private raetselDataService: RaetselDataService
  ) {}
}
