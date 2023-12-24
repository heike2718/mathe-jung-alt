import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { PageDefinition, QUERY_PARAM_LIMIT, QUERY_PARAM_OFFSET } from "@mja-ws/core/model";
import { MediensucheResult, MediensucheTrefferItem, MediumDto } from "@mja-ws/medien/model";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class MedienHttpService {

    #http = inject(HttpClient);
    #url = '/mja-api/medien';


    findMedien(suchstring: string, pageDefinition: PageDefinition): Observable<MediensucheResult> {

        const offset = pageDefinition.pageIndex * pageDefinition.pageSize;

        let params = new HttpParams()
            .set(QUERY_PARAM_LIMIT, pageDefinition.pageSize)
            .set(QUERY_PARAM_OFFSET, offset);


        if (suchstring.trim().length > 0) {

            params = params.set('suchstring', suchstring.trim())
        }

        const url = this.#url + '/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<MediensucheResult>(url, { headers, params });


    }

    findById(uuid: string): Observable<MediumDto> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        const url = this.#url + '/' + uuid + '/v1';
        return this.#http.get<MediumDto>(url, { headers });
    }

    findByTitel(titel: string): Observable<MediumDto[]> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        const url = this.#url + '/titel/v1';

        const params = new HttpParams()
            .set('suchstring', titel.trim());

        return this.#http.get<MediumDto[]>(url, { headers, params });
    }

    loadDetails(medium: MediensucheTrefferItem): Observable<MediumDto> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        const url = this.#url + '/' + medium.id + '/v1';

        return this.#http.get<MediumDto>(url, { headers});
        
    }

    saveMedium(medium: MediumDto): Observable<MediumDto> {

        const url = this.#url + '/v1';

        if ('neu' === medium.id) {
            return this.#insertMedium(url, medium);
        } else {
            return this.#updateMedium(url, medium);
        }

    }

    #insertMedium(url: string, payload: MediumDto): Observable<MediumDto> {
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.post<MediumDto>(url, payload, { headers });
    }

    #updateMedium(url: string, payload: MediumDto): Observable<MediumDto> {
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.put<MediumDto>(url, payload, { headers });
    }
}