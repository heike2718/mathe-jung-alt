import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Quelle } from '../entities/quelle';
import { Configuration, SharedConfigService } from '@mja-workspace/shared/util-configuration';
import { QUERY_PARAM_DESKRIPTOREN, QUERY_PARAM_SUCHSTRING, QUERY_PARAM_TYPE_DESKRIPTOREN, Suchfilter, SuchfilterQueryParameterMapper } from '@mja-workspace/suchfilter/domain';
import { isAdmin, User } from '@mja-workspace/shared/auth/domain';

@Injectable({ providedIn: 'root' })
export class QuellenHttpService {

  #url = this.configuration.baseUrl + '/quellen/v1';

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

  loadQuelleAdmin(): Observable<Quelle> {

    const headers = new HttpHeaders().set('Accept', 'application/json');
    const url = this.#url + '/admin';
    return this.http.get<Quelle>(url, { headers });
  }

  loadQuelle(id: string): Observable<Quelle> {

    const headers = new HttpHeaders().set('Accept', 'application/json');
    const url = this.#url + '/' + id;

    return this.http.get<Quelle>(url, { headers });
  }
}
