import { Injectable } from '@angular/core';
import { createEffect, Actions, ofType } from '@ngrx/effects';
import { map, tap } from 'rxjs/operators';
import * as RaetselActions from './raetsel.actions';
import { RaetselDataService } from '../../infrastructure/raetsel.data.service';
import { MessageService, SafeNgrxService, noopAction } from '@mja-workspace/shared/util-mja';
import { Router } from '@angular/router';
import { SuchfilterFacade } from '@mja-workspace/suchfilter/domain';
import { SearchFacade } from '../../application/search.facade';
import { AuthHttpService } from '@mja-workspace/shared/auth/domain';

@Injectable()
export class RaetselEffects {

  prepareSearch$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.prepareSearch),
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.countRaetsel(action.suchfilter).pipe(
          tap((anzahl) => this.raetselFacade.startSearch(anzahl, action.suchfilter, action.pageDefinition)),
          map(() => noopAction())
        ), 'Ups, beim Zählen der Rätsel ist etwas schiefgegangen', noopAction()
      )
    )
  );

  findRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.findRaetsel),
      // switchMap, damit spätere Sucheingaben gecanceled werden, sobald eine neue Eingabe emitted wird
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.loadPage(action.suchfilter,
          { pageIndex: action.pageDefinition.pageIndex, pageSize: action.pageDefinition.pageSize, sortDirection: action.pageDefinition.sortDirection }).pipe(
            tap(() => this.suchfilterFacade.sucheFinished(action.kontext)),
            map((raetsel) => RaetselActions.findRaetselSuccess({ raetsel }))
          ), 'Ups, beim Suchen nach Rätseln ist etwas schiefgegangen', noopAction()
      )
    ));

  selectRaetsel$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.raetselSelected),
      this.safeNgrx.safeSwitchMap((action) =>
        this.raetselDataService.findById(action.raetsel.id).pipe(
          map((raetselDetails) =>
            RaetselActions.raetselDetailsLoaded({ raetselDetails })
          )
        ), 'Ups, beim Laden der Details ist etwas schiefgegangen', noopAction()
      )
    )
  );

  showDetailsAfterLoaded$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RaetselActions.raetselDetailsLoaded),
      tap(() => {
        this.router.navigateByUrl('raetsel/details');
      }),
    ), { dispatch: false });



  constructor(
    private actions$: Actions,
    private authService: AuthHttpService,
    private raetselDataService: RaetselDataService,
    private raetselFacade: SearchFacade,
    private suchfilterFacade: SuchfilterFacade,
    private safeNgrx: SafeNgrxService,
    private messageService: MessageService,
    private router: Router
  ) { }
}
