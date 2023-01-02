import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { coreDeskriptorenActions, coreQuelleActions, fromCoreQuelle } from "@mja-ws/core/data";
import { QuelleUI } from "@mja-ws/core/model";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class CoreFacade {

    #store = inject(Store);
    #router = inject(Router);    

    existsQuelleAdmin$: Observable<boolean> = this.#store.select(fromCoreQuelle.existsQuelleAdmin);
    notExistsQuelleAdmin$: Observable<boolean> = this.#store.select(fromCoreQuelle.notExistsQuelleAdmin);
    quelleAdmin$: Observable<QuelleUI> = this.#store.select(fromCoreQuelle.quelleAdmin);

    public loadQuelleAngemeldeterAdmin(): void {
        this.#store.dispatch(coreQuelleActions.load_quelle_admin());
    }

    public loadDeskriptoren(admin: boolean): void {
        this.#store.dispatch(coreDeskriptorenActions.load_deskriptoren({ admin }));
    }

    public handleLogout(): void {
       this.#removeCoreDeskriptoren();
        this.#removeQuelleAngemeldeterAdmin;
        this.#router.navigateByUrl('/');
    }

    #removeQuelleAngemeldeterAdmin(): void {
        this.#store.dispatch(coreQuelleActions.core_quelle_admin_remove());
    }

    #removeCoreDeskriptoren(): void {
        this.#store.dispatch(coreDeskriptorenActions.core_deskriptoren_remove());
    }
}