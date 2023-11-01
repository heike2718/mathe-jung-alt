import { inject, Injectable} from '@angular/core';
import { EmbeddableImageVorschau } from '@mja-ws/embeddable-images/model';
import { fromGrafik, grafikActions } from '@mja-ws/grafik/data';
import { Message } from '@mja-ws/shared/messaging/api';
import { filterDefined } from '@mja-ws/shared/ngrx-utils';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Injectable({providedIn: 'root'})

export class GrafikFacade {

    #store = inject(Store);

    isLoaded$: Observable<boolean> = this.#store.select(fromGrafik.isLoaded);
    selectedEmbeddableImageVorschau$: Observable<EmbeddableImageVorschau> = filterDefined(this.#store.select(fromGrafik.selectedEmbeddableImageVorschau));
    pfad$: Observable<string> = filterDefined(this.#store.select(fromGrafik.grafikPfad));
    grafikHochgeladenMessage$: Observable<Message|undefined> = this.#store.select(fromGrafik.selectGrafikHochgeladenMessage);



    public vorschauLaden(relativerPfad: string): void {
        this.#store.dispatch(grafikActions.lADE_VORSCHAU({pfad: relativerPfad}));
    }

    public grafikHochgeladen(message: Message): void {
        this.#store.dispatch(grafikActions.gRAFIK_HOCHGELADEN({message}));
    }

    public clearVorschau(): void {
        this.#store.dispatch(grafikActions.cLEAR_VORSCHAU());
    }

} 