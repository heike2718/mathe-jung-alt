import { inject, Injectable } from '@angular/core';
import { PageDefinition } from '@mja-ws/core/model';
import { fromRaetsel, raetselActions } from '@mja-ws/raetsel/data';
import { RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { Store } from '@ngrx/store';

@Injectable({ providedIn: 'root' })
export class RaetselFacade {

    #store = inject(Store);

    loaded$ = this.#store.select(fromRaetsel.isLoaded);
    page$ = this.#store.select(fromRaetsel.page);

    public triggerSearch(suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition): void {

        this.#store.dispatch(raetselActions.select_page({pageDefinition}));
        this.#store.dispatch(raetselActions.find_raetsel({suchfilter, pageDefinition}));
    }

}