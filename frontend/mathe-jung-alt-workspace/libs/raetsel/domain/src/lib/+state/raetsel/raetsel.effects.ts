import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import * as RaetselActions from './raetsel.actions';
import { RaetselDataService } from '../../infrastructure/raetsel.data.service';
import { RaetselFacade } from '../../application/reaetsel.facade';

@Injectable()
export class RaetselEffects {

  findRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.findRaetsel),
      switchMap((action) =>
        this.raetselDataService.findRaetsel(action.filter).pipe(
          map((raetsel) => RaetselActions.findRaetselSuccess({ raetsel })),
          catchError((error) =>
            of(RaetselActions.findRaetselFailure({ error }))
          )
        )
      )
    ));

  selectPage$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.selectPage),
      switchMap((action) =>
        this.raetselFacade.raetselList$.pipe(
          map((raetsel) => RaetselActions.pageSelected({ raetsel: raetsel.slice(action.pageIndex * action.pageSize, (action.pageIndex + 1) * action.pageSize) }))
        )
      )
    ));

  constructor(
    private actions$: Actions,
    private raetselDataService: RaetselDataService,
    private raetselFacade: RaetselFacade
  ) { }
}
