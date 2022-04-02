import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import * as StichwortActions from './stichwort.actions';
import { StichwortDataService } from '../../infrastructure/stichwort.data.service';

@Injectable()
export class StichwortEffects {
  loadStichwort$ = createEffect(() =>
    this.actions$.pipe(
      ofType(StichwortActions.loadStichwort),
      switchMap((action) =>
        this.stichwortDataService.load().pipe(
          map((stichwort) =>
            StichwortActions.loadStichwortSuccess({ stichwort })
          ),
          catchError((error) =>
            of(StichwortActions.loadStichwortFailure({ error }))
          )
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private stichwortDataService: StichwortDataService
  ) {}
}
