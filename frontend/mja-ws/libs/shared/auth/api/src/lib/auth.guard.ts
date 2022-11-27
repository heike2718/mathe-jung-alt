import { inject, Injectable } from "@angular/core";
import { CanActivate } from "@angular/router";
import { Observable } from "rxjs";
import { AuthFacade } from "./auth.facade";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    #authFacade = inject(AuthFacade);

    canActivate(): Observable<boolean> | boolean {

        return this.#authFacade.userIsLoggedIn$;
    }
}