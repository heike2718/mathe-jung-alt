import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService } from "@mja-workspace/shared/util-configuration";
import { Message } from "@mja-workspace/shared/util-mja";
import { Observable } from "rxjs";


@Injectable(
    {providedIn: 'root'}
)
export class GrafikDataService {

    #url = this.configuration.baseUrl + '/grafiken/v1';


    constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) {}

    public loadGrafik(relativerPfad: string): Observable<Message> {

        const headers = new HttpHeaders().set('Accept', 'application/json');

        let params = new HttpParams();
        params = params.set('pfad', relativerPfad);


        const url = this.#url;

        return this.http.get<Message>(url, { headers: headers, params: params });
    }


    

}