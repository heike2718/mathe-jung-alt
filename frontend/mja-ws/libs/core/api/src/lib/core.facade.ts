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

        this.#store.dispatch(coreQuelleActions.load_quelle_admin());
    }

    public loadDeskriptoren(): void {
        if (this.#deskriptorenLoaded) {
            return;
        }
        this.#store.dispatch(coreDeskriptorenActions.load_deskriptoren());
    }

    public loadAnzahlPublicRaetsel(): void{
        this.#store.dispatch(coreStatistikActions.load_anzahl_raetsel_public());
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
        this.#store.dispatch(coreQuelleActions.core_quelle_admin_remove());
    }

    #removeCoreDeskriptoren(): void {
        this.#store.dispatch(coreDeskriptorenActions.core_deskriptoren_remove());
    }
}