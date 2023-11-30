import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { CreateEmbeddableImageRequestDto, EmbeddableImageResponseDto, EmbeddableImageVorschau, ReplaceEmbeddableImageRequestDto } from "@mja-ws/embeddable-images/model";
import { Message } from "@mja-ws/shared/messaging/api";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class EmbeddableImagesHttpService {

    #url = '/mja-api/embeddable-images';
    #httpClient = inject(HttpClient);

    loadGrafik(relativerPfad: string): Observable<EmbeddableImageVorschau> {

        const headers = new HttpHeaders().set('Accept', 'application/json');

        let params = new HttpParams();
        params = params.set('pfad', relativerPfad);

        return this.#httpClient.get<EmbeddableImageVorschau>(this.#url + '/v1', { headers: headers, params: params });
    }

    createEmbeddableImage(requestDto: CreateEmbeddableImageRequestDto): Observable<EmbeddableImageResponseDto> {

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#httpClient.post<EmbeddableImageResponseDto>(this.#url + '/v1', requestDto, {
            headers: headers
        });
    }

    replaceEmbeddableImage(requestDto: ReplaceEmbeddableImageRequestDto): Observable<EmbeddableImageResponseDto> {

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#httpClient.put<EmbeddableImageResponseDto>(this.#url + '/v1', requestDto, {
            headers: headers
        });
    }
}