import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService } from "@mja-workspace/shared/util-configuration";
import { Observable } from "rxjs";
import { Deskriptor } from "../entities/suchfilter";


@Injectable()
export class DeskriptorDataService {


    #url = this.configuration.baseUrl + '/deskriptoren/v1';

    constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

    load(): Observable<Deskriptor[]> {

        const headers = new HttpHeaders().set('Accept', 'application/json');

        if (this.configuration.admin) {
            const params = new HttpParams().set('kontext', 'NOOP');
            return this.http.get<Deskriptor[]>(this.#url, { headers, params });
        }

        return this.http.get<Deskriptor[]>(this.#url, { headers });
    }

}