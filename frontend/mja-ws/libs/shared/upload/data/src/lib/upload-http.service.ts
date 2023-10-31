import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, of } from "rxjs";
import { Message } from "@mja-ws/shared/messaging/api";
import { CreateEmbeddableImageResponse, EmbeddableImageContext } from "@mja-ws/shared/upload/model";


@Injectable({
    providedIn: 'root'
})
export class UploadHttpService {

    #url = '/mja-api/uploads/';

    #http = inject(HttpClient);

    public createEmbeddableImage(file: File, context: EmbeddableImageContext): Observable<CreateEmbeddableImageResponse>  {

        const formData = new FormData();
        formData.append('uploadedFile', file);
        formData.append('textart', context.textart!);
        formData.append('raetselId', context.raetselId);

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.put<CreateEmbeddableImageResponse>(this.#url + 'embeddable-images/v1', formData, {headers: headers});
    }

    public uploadFile(file: File, pfad: string, raetselId: string): Observable<Message> {

        const formData = new FormData();
        formData.append('uploadedFile', file);
        formData.append('pfad', pfad);
        formData.append('raetselId', raetselId);

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.post<Message>(this.#url + 'v1', formData, {
            headers: headers
        });
    }
}