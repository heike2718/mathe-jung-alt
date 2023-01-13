import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { RaetselgruppensucheTreffer, RaetselgruppenSuchparameter } from "@mja-ws/raetselgruppen/model";
import { Observable } from "rxjs";
import { PageDefinition, QUERY_PARAM_LIMIT, QUERY_PARAM_OFFSET, QUERY_PARAM_SORT_ATTRIBUTE, QUERY_PARAM_SORT_DIRECTION } from "@mja-ws/core/model";


@Injectable({ providedIn: 'root' })
export class RaetselgruppenHttpService {

    #http = inject(HttpClient);
    #url = '/raetselgruppen';

    public findRaetselgruppen(suchparameter: RaetselgruppenSuchparameter, pageDefinition: PageDefinition): Observable<RaetselgruppensucheTreffer> {

        const offset = pageDefinition.pageIndex * pageDefinition.pageSize;

        const params = new HttpParams()
            .set(QUERY_PARAM_LIMIT, pageDefinition.pageSize)
            .set(QUERY_PARAM_OFFSET, offset)
            .set(QUERY_PARAM_SORT_DIRECTION, pageDefinition.sortDirection)
            .set(QUERY_PARAM_SORT_DIRECTION, suchparameter.sortOrder)
            .set(QUERY_PARAM_SORT_ATTRIBUTE, suchparameter.sortAttribute);

        const url = this.#url + '/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<RaetselgruppensucheTreffer>(url, { headers, params });
    }
}