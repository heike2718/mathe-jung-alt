import { inject, Injectable} from '@angular/core';
import { fromGrafik, grafikActions } from '@mja-ws/grafik/data';
import { GrafikSearchResult } from '@mja-ws/grafik/model';
import { Message } from '@mja-ws/shared/messaging/api';
import { filterDefined } from '@mja-ws/shared/ngrx-utils';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Injectable({providedIn: 'root'})

export class GrafikFacade {

    #store = inject(Store);

    public isLoaded$: Observable<boolean> = this.#store.select(fromGrafik.isLoaded);
    public grafikSearchResult$: Observable<GrafikSearchResult> = filterDefined(this.#store.select(fromGrafik.selectedGrafikSearchResult));
    // public grafikSearchResult$: Observable<GrafikSearchResult | undefined> = this.#store.select(fromGrafik.selectedGrafikSearchResult);

    public grafikPruefen(relativerPfad: string): void {
        this.#store.dispatch(grafikActions.pruefe_grafik({pfad: relativerPfad}));
    }

    public grafikHochgeladen(message: Message): void {
        this.#store.dispatch(grafikActions.grafik_hochgeladen({message}));
    }

    public handleUploadError(message: Message): void {
        
    }

    public clearVorschau(): void {
        this.#store.dispatch(grafikActions.clear_vorschau());
    }

} 