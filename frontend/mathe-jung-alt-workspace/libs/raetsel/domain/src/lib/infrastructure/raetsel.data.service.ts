import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { EditRaetselPayload, Raetsel, RaetselDetails } from '../entities/raetsel';
import { PageDefinition, QUERY_PARAM_DESKRIPTOREN, QUERY_PARAM_LIMIT, QUERY_PARAM_OFFSET, QUERY_PARAM_SORT_DIRECTION, QUERY_PARAM_SUCHSTRING, QUERY_PARAM_TYPE_DESKRIPTOREN, Suchfilter, SuchfilterQueryParameterMapper, SuchfilterWithStatus } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { Configuration, SharedConfigService } from '@mathe-jung-alt-workspace/shared/configuration';

@Injectable({ providedIn: 'root' })
export class RaetselDataService {


  #url = this.configuration.admin ? this.configuration.baseUrl + '/admin/raetsel/v1' : this.configuration.baseUrl + '/raetsel/v1';


  constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

  countRaetsel(suchfilter: Suchfilter): Observable<number> {
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

  loadPage(suchfilterWithStatus: SuchfilterWithStatus, pageDefinition: PageDefinition): Observable<Raetsel[]> {

    if (!suchfilterWithStatus.nichtLeer) {
      return of([]);
    }

    const suchfilter: Suchfilter = suchfilterWithStatus.suchfilter;
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

  public saveRaetsel(editRaetselPayload: EditRaetselPayload): Observable<RaetselDetails> {

    console.log(JSON.stringify(editRaetselPayload));

    const headers = new HttpHeaders().set('Accept', 'application/json');

    if ('neu' === editRaetselPayload.raetsel.id) {
      return this.http.post<RaetselDetails>(this.#url, editRaetselPayload, { headers });
    }

    return this.http.put<RaetselDetails>(this.#url, editRaetselPayload, { headers });
  }

  public findById(uuid: string): Observable<RaetselDetails> {

    const url = this.#url + '/' + uuid;
    const headers = new HttpHeaders().set('Accept', 'application/json');
    return this.http.get<RaetselDetails>(url, { headers: headers });
  }
}


