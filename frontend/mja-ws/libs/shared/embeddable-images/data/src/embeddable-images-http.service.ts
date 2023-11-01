import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { CreateEmbeddableImageRequestDto, EmbeddableImageResponseDto, ReplaceEmbeddableImageRequestDto } from "@mja-ws/embeddable-images/model";
import { Message } from "@mja-ws/shared/messaging/api";
import { Observable } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class EmbeddableImagesHttpService {

    #url = '/mja-api/embeddable-images';
    #httpClient = inject(HttpClient);

    public createEmbeddableImage(requestDto: CreateEmbeddableImageRequestDto): Observable<EmbeddableImageResponseDto> {

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#httpClient.put<EmbeddableImageResponseDto>(this.#url + '/v1', requestDto, {
            headers: headers
        });
    }

    public replaceEmbeddableImage(requestDto: ReplaceEmbeddableImageRequestDto): Observable<Message> {

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#httpClient.post<Message>(this.#url + '/v1', requestDto, {
            headers: headers
        });
    }
}