import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService } from "@mja-workspace/shared/util-configuration";
import { LoadingIndicatorService } from "@mja-workspace/shared/util-mja";
import { Observable, of } from "rxjs";
import { EditRaetselgruppenelementPayload, EditRaetselgruppePayload, RaetselgruppeBasisdaten, RaetselgruppeDetails, RaetselgruppensucheTreffer, RaetselgruppensucheTrefferItem, RaetselgruppenSuchparameter } from "../entities/raetselgruppen";


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

    public saveRaetselgruppe(editRaetselgruppPayload: EditRaetselgruppePayload, csrfToken: string | null): Observable<RaetselgruppensucheTrefferItem> {

        let headers = new HttpHeaders().set('Accept', 'application/json');
        if (csrfToken !== null) {
          headers = headers.set(this.#csrfHeaderName, csrfToken);
        }
    
        if ('neu' === editRaetselgruppPayload.id) {
          return this.http.post<RaetselgruppensucheTrefferItem>(this.#url, editRaetselgruppPayload, { headers });
        }
    
        return this.loadingService.showLoaderUntilCompleted(this.http.put<RaetselgruppensucheTrefferItem>(this.#url, editRaetselgruppPayload, { headers }));
    }

    public findById(uuid: string): Observable<RaetselgruppeDetails> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        const url = this.#url + '/' + uuid;
        const obs$: Observable<RaetselgruppeDetails> = this.http.get<RaetselgruppeDetails>(url, {headers});

        return this.loadingService.showLoaderUntilCompleted(obs$);
    }

    public saveRaetselgruppenelement(raetselgruppeID: string, payload: EditRaetselgruppenelementPayload): Observable<RaetselgruppeDetails> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        const url = this.#url + '/' + raetselgruppeID + '/elemente';
        const obs$: Observable<RaetselgruppeDetails> = this.http.post<RaetselgruppeDetails>(url, payload, {headers});

        return this.loadingService.showLoaderUntilCompleted(obs$);
    }

}