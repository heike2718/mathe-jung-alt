import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Configuration, SharedConfigService } from "@mja-workspace/shared/util-configuration";
import { LoadingIndicatorService } from "@mja-workspace/shared/util-mja";
import { Observable } from "rxjs";
import { GeneratedImages } from "../raetsel-image/raetsel-images.model";

@Injectable({
  providedIn: 'root'
})
export class SharedHttpService {

  #url = this.configuration.baseUrl;
  // #csrfHeaderName = 'X-XSRF-TOKEN';

    constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration, private loadingService: LoadingIndicatorService){}


    loadRaetselPNGs(schluessel: string): Observable<GeneratedImages> {

        const url = this.#url + '/raetsel/v1/PNG/' + schluessel;
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.loadingService.showLoaderUntilCompleted(this.http.get<GeneratedImages>(url, { headers: headers }));
      }
    
}