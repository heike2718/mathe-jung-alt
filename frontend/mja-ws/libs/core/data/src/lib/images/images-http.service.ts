import { HttpClient, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { GeneratedImages } from "@mja-ws/core/model";
import { Observable } from "rxjs";

@Injectable({providedIn: 'root'})
export class ImagesHttpService {


    #http = inject(HttpClient);

    loadRaetselPNGs(schluessel: string): Observable<GeneratedImages> {

        const url = '/mja-api/raetsel/v1/PNG/' + schluessel;
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.get<GeneratedImages>(url, { headers: headers });
      }
}