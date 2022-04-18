import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { AuthFacade } from "@mathe-jung-alt-workspace/shared/auth/domain";
import { filter, map, Observable, tap } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(private router: Router, private authFacade: AuthFacade) { }


    canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): Observable<boolean> | boolean {

        return this.authFacade.isAuthorized$.pipe(
            tap((auth) => {
                if (!auth) {
                    this.router.navigateByUrl('/forbidden')
                }
            })
        );
    }
}