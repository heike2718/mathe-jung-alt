import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { map, Observable, tap } from "rxjs";
import { AuthFacade } from "../application/auth.facade";
import { User } from "../entities/auth.model";

@Injectable({
    providedIn: 'root'
})
export class AdminGuard implements CanActivate {

    constructor(private router: Router, private authFacade: AuthFacade) { }

    canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): Observable<boolean> | boolean {

        return this.authFacade.getUser$.pipe(
            map((user: User | undefined) => user !== undefined && user.rolle === 'ADMIN')
        );
    }
}