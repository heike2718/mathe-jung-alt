import { inject, Injectable } from "@angular/core";
import { coreQuelleActions, fromCoreQuelle } from "@mja-ws/core/data";
import { QuelleUI } from "@mja-ws/core/model";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class CoreFacade {

    #store = inject(Store);

    existsQuelleAdmin$: Observable<boolean> = this.#store.select(fromCoreQuelle.existsQuelleAdmin);
    quelleAdmin$: Observable<QuelleUI> = this.#store.select(fromCoreQuelle.quelleAdmin);

    public loadQuelleAngemeldeterAdmin(): void {
        this.#store.dispatch(coreQuelleActions.load_quelle_admin());
    }

    public removeQuelleAngemeldeterAdmin(): void {
        this.#store.dispatch(coreQuelleActions.core_quelle_admin_remove());
    }

}