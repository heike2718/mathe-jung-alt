import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Configuration, SharedConfigService } from '@mja-workspace/shared/util-configuration';
import {
  Suchfilter,
  QUERY_PARAM_SUCHSTRING,
  QUERY_PARAM_DESKRIPTOREN,
  QUERY_PARAM_TYPE_DESKRIPTOREN,
  QUERY_PARAM_LIMIT,
  QUERY_PARAM_OFFSET,
  QUERY_PARAM_SORT_DIRECTION,
  SuchfilterQueryParameterMapper,
  PageDefinition
} from '@mja-workspace/suchfilter/domain';
import { EditRaetselPayload, GeneratedImages, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, LATEX_OUTPUTFORMAT, Raetsel, RaetselDetails } from '../entities/raetsel';

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

  findById(uuid: string): Observable<RaetselDetails> {

    const url = this.#url + '/' + uuid;
    const headers = new HttpHeaders().set('Accept', 'application/json');
    return this.http.get<RaetselDetails>(url, { headers: headers });
  }

  generiereRaetselOutput(raetselId: string, outputFormat: LATEX_OUTPUTFORMAT, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): Observable<GeneratedImages> {

    const url = this.#url + '/' + outputFormat + '/' + raetselId;

    const headers = new HttpHeaders().set('Accept', 'application/json');
    const params = new HttpParams().set('layoutAntwortvorschlaege', layoutAntwortvorschlaege);

    return this.http.get<GeneratedImages>(url, { headers: headers, params: params });
  }

  saveRaetsel(editRaetselPayload: EditRaetselPayload, csrfToken: string | null): Observable<RaetselDetails> {

    // console.log(JSON.stringify(editRaetselPayload));

    let headers = new HttpHeaders().set('Accept', 'application/json');
    if (csrfToken !== null) {
      headers = headers.set(this.#csrfHeaderName, csrfToken);
    }

    if ('neu' === editRaetselPayload.raetsel.id) {
      return this.http.post<RaetselDetails>(this.#url, editRaetselPayload, { headers });
    }

    return this.http.put<RaetselDetails>(this.#url, editRaetselPayload, { headers });
  }
}
