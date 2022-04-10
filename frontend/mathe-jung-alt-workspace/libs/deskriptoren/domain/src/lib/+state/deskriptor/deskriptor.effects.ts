import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import * as DeskriptorActions from './deskriptor.actions';
import { DeskriptorDataService } from '../../infrastructure/deskriptor.data.service';

@Injectable()
export class DeskriptorEffects {
  loadDeskriptor$ = createEffect(() =>
    this.actions$.pipe(
      ofType(DeskriptorActions.loadDeskriptor),
      switchMap((action) =>
        this.deskriptorDataService.load().pipe(
          map((deskriptor) =>
            DeskriptorActions.loadDeskriptorSuccess({ deskriptor })
          ),
          catchError((error) =>
            of(DeskriptorActions.loadDeskriptorFailure({ error }))
          )
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private deskriptorDataService: DeskriptorDataService
  ) {}
}
