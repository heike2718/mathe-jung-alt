import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService } from "@mja-workspace/shared/util-configuration";
import { Message } from "@mja-workspace/shared/util-mja";
import { Observable } from "rxjs";
import { UploadComponentModel } from "./file-upload.model";


@Injectable({ providedIn: 'root' })
export class FileUploadService {

    #url = this.configuration.baseUrl + '/file-upload/v1';

    constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

    public uploadFile(file: File, uploadModel: UploadComponentModel): Observable<Message> {

        const formData = new FormData();
        formData.append('uploadedFile', file);

        const params = new HttpParams()
            .set('pfad', uploadModel.pfad)
            .set('type', uploadModel.typ);

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.http.post<Message> (this.#url, formData, {
            headers: headers,
            params: params
        });
    }

}