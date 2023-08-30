import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import {
    RaetselgruppenTreffer,
    RaetselgruppenSuchparameter,
    RaetselgruppeDetails,
    EditRaetselgruppenelementPayload,
    Raetselgruppenelement,
    RaetselgruppenTrefferItem,
    EditRaetselgruppePayload,
    RaetselgruppeBasisdaten
} from "@mja-ws/raetselgruppen/model";
import { Observable } from "rxjs";
import { GeneratedFile, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, QUERY_PARAM_LIMIT, QUERY_PARAM_OFFSET, QUERY_PARAM_SORT_ATTRIBUTE, QUERY_PARAM_SORT_DIRECTION } from "@mja-ws/core/model";


@Injectable({ providedIn: 'root' })
export class RaetselgruppenHttpService {

    #http = inject(HttpClient);
    #url = '/mja-api/raetselgruppen';

    findRaetselgruppen(suchparameter: RaetselgruppenSuchparameter, pageDefinition: PageDefinition): Observable<RaetselgruppenTreffer> {

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

    findById(uuid: string): Observable<RaetselgruppeDetails> {
        const headers = new HttpHeaders().set('Accept', 'application/json');
        const url = this.#url + '/' + uuid + '/v1';
        return this.#http.get<RaetselgruppeDetails>(url, { headers });
    }

    saveRaetselgruppe(editRaetselgruppePayload: EditRaetselgruppePayload): Observable<RaetselgruppeBasisdaten> {

       const url = this.#url + '/v1';

        if ('neu' === editRaetselgruppePayload.id) {
            return this.#insertRaetselgruppe(url, editRaetselgruppePayload);
        } else {
            return this.#updateRaetselgruppe(url, editRaetselgruppePayload);
        }
    }

    saveRaetselgruppenelement(raetselgruppeID: string, payload: EditRaetselgruppenelementPayload): Observable<RaetselgruppeDetails> {

        const url = this.#url + '/' + raetselgruppeID + '/elemente/v1';

        if (payload.id === 'neu') {
            return this.#insertRaetselgruppenelement(url, payload);
        } else {
            return this.#updateRaetselgruppenelement(url, payload);
        }
    }

    deleteRaetselgruppenelement(raetselgruppeID: string, payload: Raetselgruppenelement): Observable<RaetselgruppeDetails> {
        
        const url = this.#url + '/' + raetselgruppeID + '/elemente/' + payload.id + '/v1';

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.delete<RaetselgruppeDetails>(url, { headers });
    }

    generiereVorschau(raetselgruppeID: string): Observable<GeneratedFile> {


        const url = this.#url + '/vorschau/' + raetselgruppeID + '/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<GeneratedFile>(url, { headers: headers });

    }

    generiereLaTeX(raetselgruppeID: string): Observable<GeneratedFile> {

        const url = this.#url + '/latex/' + raetselgruppeID + '/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<GeneratedFile>(url, { headers: headers });
    }

    #insertRaetselgruppe(url: string, payload: EditRaetselgruppePayload): Observable<RaetselgruppeBasisdaten> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.post<RaetselgruppeBasisdaten>(url, payload, { headers });

    }

    #updateRaetselgruppe(url: string, payload: EditRaetselgruppePayload): Observable<RaetselgruppeBasisdaten> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.put<RaetselgruppeBasisdaten>(url, payload, { headers });

    }

    #insertRaetselgruppenelement(url: string, payload: EditRaetselgruppenelementPayload): Observable<RaetselgruppeDetails> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.post<RaetselgruppeDetails>(url, payload, { headers });
    }

    #updateRaetselgruppenelement(url: string, payload: EditRaetselgruppenelementPayload): Observable<RaetselgruppeDetails> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.put<RaetselgruppeDetails>(url, payload, { headers });
    }
}