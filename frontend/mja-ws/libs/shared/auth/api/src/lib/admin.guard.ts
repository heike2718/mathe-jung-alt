import { inject, Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { Observable, tap } from "rxjs";
import { AuthFacade } from "./auth.facade";

@Injectable({
    providedIn: 'root'
})
export class AdminGuard implements CanActivate {

    #authFacade = inject(AuthFacade);
    #router = inject(Router);

    canActivate(): Observable<boolean> | boolean {

        return this.#authFacade.userIsAdmin$.pipe(
            tap((admin) => {
                if (!admin) {
                    this.#router.navigateByUrl('/')
                }
            })
        );
    }

}