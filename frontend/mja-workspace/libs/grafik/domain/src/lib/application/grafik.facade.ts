import { Injectable } from '@angular/core';
import * as GrafikActions from '../+state/grafik.actions';
import * as GrafikSelectors from '../+state/grafik.selectors';
import * as fromGrafik from '../+state/grafik.reducer';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { GrafikSearchResult } from '../entities/grafik.model';


@Injectable({
    providedIn: 'root'
})
export class GrafikFacade {

    public isLoading$: Observable<boolean> = this.store.pipe(select(GrafikSelectors.isLoading));
    public isLoaded$: Observable<boolean> = this.store.pipe(select(GrafikSelectors.isLoaded));
    public getSelectedGrafikSearchResult$: Observable<GrafikSearchResult> = this.store.pipe(select(GrafikSelectors.getSelectedGrafikSearchResult));

    constructor(private store: Store<fromGrafik.GrafikState> ) {}

    public grafikPruefen(relativerPfad: string): void {
        this.store.dispatch(GrafikActions.pruefeGrafik({pfad: relativerPfad}));
    }
}