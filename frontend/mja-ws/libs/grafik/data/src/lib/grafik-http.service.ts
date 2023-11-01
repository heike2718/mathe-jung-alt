import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { EmbeddableImageVorschau } from "@mja-ws/embeddable-images/model";
import { GrafikSearchResult } from "@mja-ws/grafik/model";
import { Observable } from "rxjs";

@Injectable({providedIn: 'root'})
export class GrafikHttpService {

    #http = inject(HttpClient);
    #url = '/mja-api/embeddable-images/v1';

    public loadGrafik(relativerPfad: string): Observable<EmbeddableImageVorschau> {

        const headers = new HttpHeaders().set('Accept', 'application/json');

        let params = new HttpParams();
        params = params.set('pfad', relativerPfad);

        return this.#http.get<EmbeddableImageVorschau>(this.#url, { headers: headers, params: params });
    }

}