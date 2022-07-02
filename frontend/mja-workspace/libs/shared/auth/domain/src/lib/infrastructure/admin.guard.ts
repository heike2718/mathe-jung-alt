import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { map, Observable, tap } from "rxjs";
import { AuthFacade } from "../application/auth.facade";
import { isAdmin, User } from "../entities/auth.model";

@Injectable({
    providedIn: 'root'
})
export class AdminGuard implements CanActivate {

    constructor(private _router: Router, private authFacade: AuthFacade) { }

    canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): Observable<boolean> | boolean {

        return this.authFacade.getUser$.pipe(
            map((user: User | undefined) => isAdmin(user))
        );
    }
}