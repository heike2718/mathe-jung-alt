import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from "@angular/router";
import { map, Observable } from "rxjs";
import { RaetselFacade } from "../application/raetsel.facade";

@Injectable({ providedIn: 'root' })
export class RaetselDetailsGuard implements CanActivate {

    constructor(private raetselFacade: RaetselFacade) { }



    canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {


        return this.raetselFacade.raetselDetails$.pipe(
            map((details) => details !== undefined)
        )
    }

}