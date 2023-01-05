import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { DeskriptorUI,
    PageDefinition,
    QUERY_PARAM_DESKRIPTOREN,
    QUERY_PARAM_LIMIT,
    QUERY_PARAM_OFFSET,
    QUERY_PARAM_SORT_DIRECTION,
    QUERY_PARAM_SUCHSTRING,
    QUERY_PARAM_TYPE_DESKRIPTOREN 
} from "@mja-ws/core/model";
import { Raetsel, RaetselDetails, RaetselsucheTreffer, RaetselSuchfilter } from "@mja-ws/raetsel/model";
import { Configuration } from "@mja-ws/shared/config";
import { Observable, of } from "rxjs";

@Injectable({ providedIn: 'root' })
export class RaetselHttpService {

   #http = inject(HttpClient);

    #url = '/raetsel';

    public findRaetsel(suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition): Observable<RaetselsucheTreffer> {

        const offset = pageDefinition.pageIndex * pageDefinition.pageSize;

        let params = new HttpParams()
            .set(QUERY_PARAM_LIMIT, pageDefinition.pageSize)
            .set(QUERY_PARAM_OFFSET, offset)
            .set(QUERY_PARAM_SORT_DIRECTION, pageDefinition.sortDirection);

        if (suchfilter.suchstring && suchfilter.suchstring.trim().length > 0) {
            params = params.set(QUERY_PARAM_SUCHSTRING, suchfilter.suchstring.trim());
        }

        if (suchfilter.deskriptoren.length > 0) {
            params = params.set(QUERY_PARAM_DESKRIPTOREN, this.#getDeskriptoren(suchfilter));
        }

        params = params.set(QUERY_PARAM_TYPE_DESKRIPTOREN, 'ORDINAL');

        const url = this.#url + '/admin/v2';
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.get<RaetselsucheTreffer>(url, { headers, params });
    }

    public loadRaetselDetails(raetsel: Raetsel): Observable<RaetselDetails> {

        const url = this.#url + '/v1/' + raetsel.id;
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<RaetselDetails>(url, {headers: headers});        
    }


    #getDeskriptoren(suchfilter: RaetselSuchfilter): string {

        let result = '';
        for (let index = 0; index < suchfilter.deskriptoren.length; index++) {

            const deskriptor: DeskriptorUI = suchfilter.deskriptoren[index];

            if (index < suchfilter.deskriptoren.length - 1) {
                result += deskriptor.id + ',';
            } else {
                result += deskriptor.id
            }
        }
        return result;
    }

}