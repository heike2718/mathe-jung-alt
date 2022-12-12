import { inject, Injectable } from "@angular/core";
import { coreQuelleActions } from "@mja-ws/core/data";
import { Store } from "@ngrx/store";


@Injectable({
    providedIn: 'root'
})
export class CoreFacade {

    #store = inject(Store);

    public loadQuelleAngemeldeterAdmin(): void {
        this.#store.dispatch(coreQuelleActions.load_quelle_admin());
    }

}