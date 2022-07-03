import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Configuration, SharedConfigService } from '@mja-workspace/shared/util-configuration';
import { Suchfilter,
  QUERY_PARAM_SUCHSTRING,
  QUERY_PARAM_DESKRIPTOREN,
  QUERY_PARAM_TYPE_DESKRIPTOREN,
  QUERY_PARAM_LIMIT,
  QUERY_PARAM_OFFSET,
  QUERY_PARAM_SORT_DIRECTION,
  SuchfilterQueryParameterMapper,
  PageDefinition
} from '@mja-workspace/suchfilter/domain';
import { Raetsel } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselDataService {

  #url = this.configuration.baseUrl + '/raetsel/v1';
  #csrfHeaderName = 'X-XSRF-TOKEN';
  
  constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

  countRaetsel(suchfilter: Suchfilter | undefined): Observable<number> {

    if (suchfilter === undefined) {
      return of(0);
    }

    if (suchfilter.deskriptoren.length === 0 && (suchfilter.suchstring === undefined || suchfilter.suchstring.trim().length === 0)) {
      return of(0);
    }

    const mapper = new SuchfilterQueryParameterMapper(suchfilter);

    let params = new HttpParams();

    if (suchfilter.suchstring && suchfilter.suchstring.trim().length > 0) {
      params = params.set(QUERY_PARAM_SUCHSTRING, suchfilter.suchstring.trim());
    }

    if (suchfilter.deskriptoren.length > 0) {
      params = params.set(QUERY_PARAM_DESKRIPTOREN, mapper.getDeskriptoren());
    }

    params = params.set(QUERY_PARAM_TYPE_DESKRIPTOREN, 'ORDINAL');

    const headers = new HttpHeaders().set('Accept', 'text/plain');
    const url = this.#url + '/size';

    return this.http.get<number>(url, { headers, params });
  }

  loadPage(suchfilter: Suchfilter | undefined, pageDefinition: PageDefinition): Observable<Raetsel[]> {

    if (suchfilter === undefined) {
      return of([]);
    }
    
    const offset = pageDefinition.pageIndex * pageDefinition.pageSize;

    let params = new HttpParams()
      .set(QUERY_PARAM_LIMIT, pageDefinition.pageSize)
      .set(QUERY_PARAM_OFFSET, offset)
      .set(QUERY_PARAM_SORT_DIRECTION, pageDefinition.sortDirection);

    const mapper = new SuchfilterQueryParameterMapper(suchfilter);

    if (suchfilter.suchstring && suchfilter.suchstring.trim().length > 0) {
      params = params.set(QUERY_PARAM_SUCHSTRING, suchfilter.suchstring.trim());
    }

    if (suchfilter.deskriptoren.length > 0) {
      params = params.set(QUERY_PARAM_DESKRIPTOREN, mapper.getDeskriptoren());
    }

    params = params.set(QUERY_PARAM_TYPE_DESKRIPTOREN, 'ORDINAL');
    
    const headers = new HttpHeaders().set('Accept', 'application/json');
    return this.http.get<Raetsel[]>(this.#url, { headers, params });
  }
}
