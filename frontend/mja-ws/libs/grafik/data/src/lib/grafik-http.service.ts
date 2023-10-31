import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { GrafikSearchResult } from "@mja-ws/grafik/model";
import { Observable } from "rxjs";

@Injectable({providedIn: 'root'})
export class GrafikHttpService {

    #http = inject(HttpClient);
    #url = '/mja-api/embeddable-images/v1';

    public loadGrafik(relativerPfad: string): Observable<GrafikSearchResult> {

        const headers = new HttpHeaders().set('Accept', 'application/json');

        let params = new HttpParams();
        params = params.set('pfad', relativerPfad);

        return this.#http.get<GrafikSearchResult>(this.#url, { headers: headers, params: params });
    }

}