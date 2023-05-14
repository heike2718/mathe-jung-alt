import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Message } from "@mja-ws/shared/messaging/api";
import { Observable, catchError } from "rxjs";
import { UploadComponentModel } from "./file-upload.model";

@Injectable({ providedIn: 'root' })
export class FileUploadService {

    #url = '/uploads/v1';

    #http = inject(HttpClient);

    public uploadFile(file: File, uploadModel: UploadComponentModel): Observable<Message> {

        const formData = new FormData();
        formData.append('uploadedFile', file);

        const params = new HttpParams()
            .set('pfad', uploadModel.pfad);

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.post<Message>(this.#url, formData, {
            headers: headers,
            params: params
        });
    }
}