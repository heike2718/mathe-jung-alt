import { inject, Injectable } from "@angular/core";
import { fromRaetselgruppen } from "@mja-ws/raetselgruppen/data";
import { RaetselgruppenTrefferItem } from "@mja-ws/raetselgruppen/model";
import { select, Store } from "@ngrx/store";
import { Observable } from "rxjs";


@Injectable({providedIn: 'root'})
export class RaetselgruppenFacade {

    #store = inject(Store);

    page$: Observable<RaetselgruppenTrefferItem[]> = this.#store.select(fromRaetselgruppen.page);

}