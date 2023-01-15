import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { RaetselgruppenTreffer, RaetselgruppenSuchparameter, RaetselgruppeDetails } from "@mja-ws/raetselgruppen/model";
import { Observable } from "rxjs";
import { PageDefinition, QUERY_PARAM_LIMIT, QUERY_PARAM_OFFSET, QUERY_PARAM_SORT_ATTRIBUTE, QUERY_PARAM_SORT_DIRECTION } from "@mja-ws/core/model";


@Injectable({ providedIn: 'root' })
export class RaetselgruppenHttpService {

    #http = inject(HttpClient);
    #url = '/raetselgruppen';

    public findRaetselgruppen(suchparameter: RaetselgruppenSuchparameter, pageDefinition: PageDefinition): Observable<RaetselgruppenTreffer> {

        const offset = pageDefinition.pageIndex * pageDefinition.pageSize;

        let params = new HttpParams()
            .set(QUERY_PARAM_LIMIT, pageDefinition.pageSize)
            .set(QUERY_PARAM_OFFSET, offset)
            .set(QUERY_PARAM_SORT_DIRECTION, pageDefinition.sortDirection)
            .set(QUERY_PARAM_SORT_ATTRIBUTE, suchparameter.sortAttribute);

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

        const url = this.#url + '/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<RaetselgruppenTreffer>(url, { headers, params });
    }

    public findById(uuid: string): Observable<RaetselgruppeDetails> {
        const headers = new HttpHeaders().set('Accept', 'application/json');
        const url = this.#url + '/v1/' + uuid;
        return this.#http.get<RaetselgruppeDetails>(url, { headers });
    }
}