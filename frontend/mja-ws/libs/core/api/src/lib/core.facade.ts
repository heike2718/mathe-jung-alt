import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { coreDeskriptorenActions, coreAutorActions, fromCoreAutor, fromCoreDeskriptoren, ImagesHttpService, fromStatistik, coreStatistikActions } from "@mja-ws/core/data";
import { DeskriptorUI, GeneratedImages, QuelleDto } from "@mja-ws/core/model";
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
    #autorLoaded = false;

    existsQuelleEigenkreation$: Observable<boolean> = this.#store.select(fromCoreAutor.existsQuelleEigenkreation);
    notExistsQuelleEigenkreation$: Observable<boolean> = this.#store.select(fromCoreAutor.notExistsQuelleEigenkreation);    

    autorLoaded$: Observable<boolean> = this.#store.select(fromCoreAutor.quelleEigenkreationLoaded);
        autor$: Observable<QuelleDto> = this.#store.select(fromCoreAutor.quelleEigenkreation);


    deskriptorenUILoaded$: Observable<boolean> = this.#store.select(fromCoreDeskriptoren.isDeskriptorenUILoaded);
    alleDeskriptoren$: Observable<DeskriptorUI[]> = this.#store.select(fromCoreDeskriptoren.deskriptotrenUI);

    anzahlPublicRaetselLoaded$: Observable<boolean> = this.#store.select(fromStatistik.isAnzahlPublicRaetselLoaded);
    anzahlPublicRaetsel$: Observable<number> = this.#store.select(fromStatistik.anzahlPublicRaetsel);



    constructor() {
        this.deskriptorenUILoaded$.pipe(
            tap((loaded: boolean) => this.#deskriptorenLoaded = loaded)
        ).subscribe();

        this.autorLoaded$.pipe(
            tap((loaded) => this.#autorLoaded = loaded)
        ).subscribe();
       
    }

    public loadAutor(): void {

        if (this.#autorLoaded) {
            return;
        }

        this.#store.dispatch(coreAutorActions.lOAD_AUTOR());
    }

    public replaceAutor(quelle: QuelleDto): void {

        this.#store.dispatch(coreAutorActions.cORE_AUTOR_REPLACED({quelle: quelle}));
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
        this.#removeAutor;
        this.#router.navigateByUrl('/');
    }

    public loadRaetselPNGs(schluessel: string): Observable<GeneratedImages> {
        return this.#imagesHttpService.loadRaetselPNGs(schluessel);
    }

    #removeAutor(): void {
        this.#store.dispatch(coreAutorActions.rEMOVE_AUTOR());
        console.log('coreQuelleActions.rEMOVE_AUTOR() called')
    }

    #removeCoreDeskriptoren(): void {       
        this.#store.dispatch(coreDeskriptorenActions.cORE_DESKRIPTOREN_REMOVE());
        
    }
}