import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from "@angular/router";
import { map, Observable } from "rxjs";
import { QuellenFacade } from "../application/quellen.facade";

@Injectable({ providedIn: 'root' })
export class QuellenDetailsGuard implements CanActivate {

    constructor(private quellenFacade: QuellenFacade) { }



    canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {


        return this.quellenFacade.selectedQuelle$.pipe(
            map((quelle) => quelle !== undefined)
        )
    }

}