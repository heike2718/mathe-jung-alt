import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { Message } from "@mja-ws/shared/messaging/api";


@Injectable({
    providedIn: 'root'
})
export class UploadHttpService {

    #url = '/mja-api/uploads/v1';

    #http = inject(HttpClient);

    public uploadFile(file: File, pfad: string, raetselId: string): Observable<Message> {

        const formData = new FormData();
        formData.append('uploadedFile', file);
        formData.append('pfad', pfad);
        formData.append('raetselId', raetselId);

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.post<Message>(this.#url, formData, {
            headers: headers
        });
    }
}