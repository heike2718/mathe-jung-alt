import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Quelle } from '../entities/quelle';
import { Configuration, SharedConfigService } from '@mathe-jung-alt-workspace/shared/configuration';
import { Suchfilter, SuchfilterQueryParameterMapper } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';

@Injectable({ providedIn: 'root' })
export class QuelleDataService {


  #url = this.configuration.admin ? this.configuration.baseUrl + '/admin/quellen' : this.configuration.baseUrl + '/quellen';

  constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

  findQuellen(suchfilter: Suchfilter): Observable<Quelle[]> {

    const headers = new HttpHeaders().set('Accept', 'application/json');
    let url = this.#url + new SuchfilterQueryParameterMapper(suchfilter).apply();
    return this.http.get<Quelle[]>(url, { headers });
  }
}
