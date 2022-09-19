import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService } from "@mja-workspace/shared/util-configuration";
import { LoadingIndicatorService } from "@mja-workspace/shared/util-mja";
import { Observable, of } from "rxjs";
import { RaetselgruppensucheTreffer, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter, Referenztyp, Schwierigkeitsgrad } from "../entities/raetselgruppen";


@Injectable(
    {
        providedIn: 'root'
    }
)
export class RaetselgruppenHttpService {

    #url = this.configuration.baseUrl + '/raetselgruppen/v1';
    #csrfHeaderName = 'X-XSRF-TOKEN';

    constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration, private loadingService: LoadingIndicatorService) { }


    public findGruppen(suchparameter: RaetselgruppenSuchparameter): Observable<RaetselgruppensucheTreffer> {

        let params = new HttpParams();

        if (suchparameter.name && suchparameter.name.trim().length > 0) {
            params = params.set('name', suchparameter.name.trim());
        }

        if (suchparameter.schwierigkeitsgrad) {
            params = params.set('schwierigkeitsgrad', suchparameter.schwierigkeitsgrad);
        }

        if (suchparameter.referenztyp) {
            params = params.set('referenztyp', suchparameter.referenztyp);
        }

        if (suchparameter.referenz && suchparameter.referenz.trim().length > 0) {
            params = params.set('referenz', suchparameter.referenz.trim());
        }

        params = params.set('sortAttribute', suchparameter.sortAttribute);
        params = params.set('sortDirection', suchparameter.sortOrder.toString());
        params = params.set('limit', suchparameter.pageSize);
        params = params.set('offset', suchparameter.pageIndex);

        const headers = new HttpHeaders().set('Accept', 'application/json');

        const obs$: Observable<RaetselgruppensucheTreffer> = this.http.get<RaetselgruppensucheTreffer>(this.#url, { headers, params })

        return this.loadingService.showLoaderUntilCompleted(obs$);
    }

    // public findGruppen(suchparameter: RaetselgruppenSuchparameter): Observable<RaetselgruppensucheTreffer> {

    //     console.log('start mocking result');
    //     const items: RaetselgruppensucheTrefferItem[] = [];
    //     items.push({
    //         anzahlElemente: 0,
    //         id: 'uuid-1',
    //         status: 'ERFASST',
    //         schwierigkeitsgrad: 'EINS',
    //         name: 'Blubbdi',
    //         referenztyp: 'MINIKAENGURU',
    //         referenz: '2022'
    //     });

    //     items.push({
    //         anzahlElemente: 0,
    //         id: 'uuid-1',
    //         status: 'ERFASST',
    //         schwierigkeitsgrad: 'ZWEI',
    //         name: 'Blubbdi bla',
    //         referenztyp: 'MINIKAENGURU',
    //         referenz: '2022'
    //     });

    //     const result: RaetselgruppensucheTreffer = {
    //         anzahlTreffer: 24,
    //         items: items
    //     };

    //     console.log('before return' + result);

    //     return of(result);

    // }

    // #getItems(): RaetselgruppensucheTrefferItem[] {

    //     const result: RaetselgruppensucheTrefferItem[] = [];

    //     for (let index = 0; index++; index < 15) {

    //         const jahr = 2000 + index;

    //         const item: RaetselgruppensucheTrefferItem = {
    //             anzahlElemente: 0,
    //             id: '' + index,
    //             status: 'ERFASST',
    //             name: 'Name ' + index,
    //             referenztyp: 'MINIKAENGURU',
    //             referenz: '' + jahr
    //         };

    //         result.push(item);
    //     }

    //     for (let index = 15; index++; index < 20) {

    //         const item: RaetselgruppensucheTrefferItem = {
    //             anzahlElemente: 0,
    //             id: '' + index,
    //             status: 'ERFASST',
    //             name: 'Name ' + index,
    //             referenztyp: 'SERIE',
    //             referenz: '' + index
    //         };

    //         result.push(item);
    //     }

    //     console.log('mocked item created');

    //     return result;
    // }


}