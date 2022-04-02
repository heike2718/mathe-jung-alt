import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import * as MedienActions from './medien.actions';
import { MedienDataService } from '../../infrastructure/medien.data.service';

@Injectable()
export class MedienEffects {
  loadMedien$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MedienActions.loadMedien),
      switchMap((action) =>
        this.medienDataService.load().pipe(
          map((medien) => MedienActions.loadMedienSuccess({ medien })),
          catchError((error) => of(MedienActions.loadMedienFailure({ error })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private medienDataService: MedienDataService
  ) {}
}
