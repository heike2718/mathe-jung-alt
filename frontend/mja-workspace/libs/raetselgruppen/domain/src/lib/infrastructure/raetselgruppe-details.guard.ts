import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from "@angular/router";
import { map, Observable, tap } from "rxjs";
import { RaetselgruppenFacade } from "../application/raetselgruppen.facade";

@Injectable({ providedIn: 'root' })
export class RaetselgruppeDetailsGuard implements CanActivate {

    constructor(private raetselgruppenFacade: RaetselgruppenFacade) { }


    canActivate(_route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
       
        return this.raetselgruppenFacade.editorContent$.pipe(
            // tap((details) => console.log(details)),
            map((details) => details !== undefined)
        );
    }

}