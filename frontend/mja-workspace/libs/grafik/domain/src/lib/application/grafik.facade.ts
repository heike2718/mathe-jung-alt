import { Injectable } from '@angular/core';
import * as GrafikActions from '../+state/grafik.actions';
import * as GrafikSelectors from '../+state/grafik.selectors';
import * as fromGrafik from '../+state/grafik.reducer';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Message } from '@mja-workspace/shared/util-mja';


@Injectable({
    providedIn: 'root'
})
export class GrafikFacade {

    public isLoading$: Observable<boolean> = this.store.pipe(select(GrafikSelectors.isLoading));
    public isLoaded$: Observable<boolean> = this.store.pipe(select(GrafikSelectors.isLoaded));
    public getGrafikSearchResult$: Observable<Message> = this.store.pipe(select(GrafikSelectors.getGrafikSearchResult));

    constructor(private store: Store<fromGrafik.GrafikState> ) {}

    public grafikPruefen(relativerPfad: string): void {
        this.store.dispatch(GrafikActions.pruefeGrafik({pfad: relativerPfad}));
    }

}