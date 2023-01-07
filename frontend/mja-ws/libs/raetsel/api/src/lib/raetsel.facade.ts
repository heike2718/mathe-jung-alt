import { inject, Injectable } from '@angular/core';
import { LATEX_LAYOUT_ANTWORTVORSCHLAEGE, OUTPUTFORMAT, PageDefinition, PaginationState } from '@mja-ws/core/model';
import { fromRaetsel, raetselActions } from '@mja-ws/raetsel/data';
import { Raetsel, RaetselDetails, RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { deepClone, filterDefined } from '@mja-ws/shared/ngrx-utils';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RaetselFacade {

    #store = inject(Store);

    loaded$: Observable<boolean> = this.#store.select(fromRaetsel.isLoaded);
    page$: Observable<Raetsel[]> = this.#store.select(fromRaetsel.page);
    paginationState$: Observable<PaginationState> = this.#store.select(fromRaetsel.paginationState);

    raetselDetails$: Observable<RaetselDetails> = this.#store.select(fromRaetsel.raetselDetails).pipe(filterDefined, deepClone);

    public triggerSearch(suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition): void {

        this.#store.dispatch(raetselActions.select_page({ pageDefinition }));
        this.#store.dispatch(raetselActions.find_raetsel({ suchfilter, pageDefinition }));
    }

    public selectRaetsel(raetsel: Raetsel): void {
        this.#store.dispatch(raetselActions.raetsel_selected({ raetsel }));
    }

    public generiereRaetselOutput(raetselID: string, outputFormat: OUTPUTFORMAT, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {

        switch(outputFormat) {
          case 'PNG': this.#store.dispatch(raetselActions.generate_raetsel_png({ raetselID, layoutAntwortvorschlaege })); break;
          case 'PDF': this.#store.dispatch(raetselActions.generate_raetsel_pdf({ raetselID, layoutAntwortvorschlaege })); break;
          default: throw new Error('Unbekanntes outputFormat ' + outputFormat);
        }    
      }

}