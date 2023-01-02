import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { coreDeskriptorenActions, coreQuelleActions, fromCoreQuelle } from "@mja-ws/core/data";
import { QuelleUI } from "@mja-ws/core/model";
import { AuthFacade } from "@mja-ws/shared/auth/api";
import { MessageService } from "@mja-ws/shared/messaging/api";
import { Store } from "@ngrx/store";
import { authActions } from "libs/shared/auth/data/src/lib/auth.actions";
import { Observable, tap } from "rxjs";



@Injectable({
    providedIn: 'root'
})
export class CoreFacade {

    #store = inject(Store);
    #authFacade = inject(AuthFacade);
    #messageService = inject(MessageService);
    #router = inject(Router);    

    constructor() {

        this.#authFacade.user$.pipe(
            tap((user) => {
                if (!user.anonym) {
                    this.#store.dispatch(coreDeskriptorenActions.load_deskriptoren({ admin: user.isAdmin }));
                }
            })
        ).subscribe();
    }

    existsQuelleAdmin$: Observable<boolean> = this.#store.select(fromCoreQuelle.existsQuelleAdmin);
    quelleAdmin$: Observable<QuelleUI> = this.#store.select(fromCoreQuelle.quelleAdmin);

    public loadQuelleAngemeldeterAdmin(): void {
        this.#store.dispatch(coreQuelleActions.load_quelle_admin());
    }

    public handleLogout(): void {
       this.#internalHandleLogout();
    }

    public handleSessionExpired(): void {
        this.#internalHandleLogout();
        this.#messageService.warn('Die Session ist abgelaufen. Bitte erneut einloggen.');
        this.#store.dispatch(authActions.logged_out());
    }

    #internalHandleLogout() {
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