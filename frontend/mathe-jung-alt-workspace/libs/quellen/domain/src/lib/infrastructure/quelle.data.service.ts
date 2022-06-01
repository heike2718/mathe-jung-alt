import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Quelle } from '../entities/quelle';
import { Configuration, SharedConfigService } from '@mathe-jung-alt-workspace/shared/configuration';
import { QUERY_PARAM_DESKRIPTOREN, QUERY_PARAM_SUCHSTRING, QUERY_PARAM_TYPE_DESKRIPTOREN, Suchfilter, SuchfilterQueryParameterMapper } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';

@Injectable({ providedIn: 'root' })
export class QuelleDataService {


  #url = this.configuration.admin ? this.configuration.baseUrl + '/admin/quellen/v1' : this.configuration.baseUrl + '/quellen/v1';

  constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

  findQuellen(suchfilter: Suchfilter): Observable<Quelle[]> {

    const mapper = new SuchfilterQueryParameterMapper(suchfilter);

    let params = new HttpParams();

    if (suchfilter.suchstring && suchfilter.suchstring.trim().length > 0) {
      params = params.set(QUERY_PARAM_SUCHSTRING, suchfilter.suchstring.trim());
    }

    if (suchfilter.deskriptoren.length > 0) {
      params = params.set(QUERY_PARAM_DESKRIPTOREN, mapper.getDeskriptoren());
    }

    params = params.set(QUERY_PARAM_TYPE_DESKRIPTOREN, 'ORDINAL');

    const headers = new HttpHeaders().set('Accept', 'application/json');
    return this.http.get<Quelle[]>(this.#url, { headers, params });
  }

  loadQuelle(id: string): Observable<Quelle> {

    const headers = new HttpHeaders().set('Accept', 'application/json');
    const url = this.#url + '/' + id;

    return this.http.get<Quelle>(url, { headers });
  }
}
