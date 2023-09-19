import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { coreDeskriptorenActions, coreQuelleActions, fromCoreQuelle, fromCoreDeskriptoren, ImagesHttpService, fromStatistik, coreStatistikActions } from "@mja-ws/core/data";
import { DeskriptorUI, GeneratedImages, QuelleUI } from "@mja-ws/core/model";
import { BENUTZERART, User } from "@mja-ws/shared/auth/model";
import { Store } from "@ngrx/store";
import { Observable, tap } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class CoreFacade {

    #store = inject(Store);
    #router = inject(Router);
    #imagesHttpService = inject(ImagesHttpService);

    #deskriptorenLoaded = false;

    existsQuelleAdmin$: Observable<boolean> = this.#store.select(fromCoreQuelle.existsQuelleAdmin);
    notExistsQuelleAdmin$: Observable<boolean> = this.#store.select(fromCoreQuelle.notExistsQuelleAdmin);
    quelleAdmin$: Observable<QuelleUI> = this.#store.select(fromCoreQuelle.quelleAdmin);

    deskriptorenUILoaded$: Observable<boolean> = this.#store.select(fromCoreDeskriptoren.isDeskriptorenUILoaded);
    alleDeskriptoren$: Observable<DeskriptorUI[]> = this.#store.select(fromCoreDeskriptoren.deskriptotrenUI);

    anzahlPublicRaetselLoaded$: Observable<boolean> = this.#store.select(fromStatistik.isAnzahlPublicRaetselLoaded);
    anzahlPublicRaetsel$: Observable<number> = this.#store.select(fromStatistik.anzahlPublicRaetsel);

    constructor() {
        this.deskriptorenUILoaded$.pipe(
            tap((loaded: boolean) => this.#deskriptorenLoaded = loaded)
        ).subscribe();
    }

    public loadQuelleAngemeldeterAdmin(benutzerart: BENUTZERART): void {

        if (benutzerart === 'ANONYM' || benutzerart === 'STANDARD') {
            return;
        }

        this.#store.dispatch(coreQuelleActions.lOAD_QUELLE_ADMIN());
    }

    public loadDeskriptoren(): void {
        if (this.#deskriptorenLoaded) {
            return;
        }
        this.#store.dispatch(coreDeskriptorenActions.lOAD_DESKRIPTOREN());
    }

    public loadAnzahlPublicRaetsel(): void{
        this.#store.dispatch(coreStatistikActions.lOAD_ANZAHL_RAETSEL_PUBLIC());
    }

    public handleLogout(): void {
        this.#removeCoreDeskriptoren();
        this.#removeQuelleAngemeldeterAdmin;
        this.#router.navigateByUrl('/');
    }

    public loadRaetselPNGs(schluessel: string): Observable<GeneratedImages> {
        return this.#imagesHttpService.loadRaetselPNGs(schluessel);
    }

    #removeQuelleAngemeldeterAdmin(): void {
        this.#store.dispatch(coreQuelleActions.cORE_QUELLE_ADMIN_REMOVE());
    }

    #removeCoreDeskriptoren(): void {
        this.#store.dispatch(coreDeskriptorenActions.cORE_DESKRIPTOREN_REMOVE());
    }
}