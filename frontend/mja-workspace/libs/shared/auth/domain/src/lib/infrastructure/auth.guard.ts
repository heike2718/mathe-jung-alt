import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { Observable, tap } from "rxjs";
import { AuthFacade } from "../application/auth.facade";


@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(private router: Router, private authFacade: AuthFacade) { }


    canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): Observable<boolean> | boolean {

        return this.authFacade.isLoggedIn$.pipe(
            tap((auth) => {
                if (!auth) {
                    this.router.navigateByUrl('/forbidden')
                }
            })
        );
    }
}
