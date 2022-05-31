import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from "@angular/router";
import { RaetselFacade } from "@mathe-jung-alt-workspace/raetsel/domain";
import { map, Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class RaetselDetailsGuard implements CanActivate {

    constructor(private raetselFacade: RaetselFacade) { }



    canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {


        return this.raetselFacade.raetselDetails$.pipe(
            map((details) => details !== undefined)
        )
    }

}