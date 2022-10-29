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
import { EditRaetselPayload, GeneratedImages, GeneratedPDF, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, RaetselDetails, RaetselsucheTreffer } from '../entities/raetsel';
import { LoadingIndicatorService } from '@mja-workspace/shared/util-mja';

@Injectable({ providedIn: 'root' })
export class RaetselHttpService {

  #url = this.configuration.baseUrl + '/raetsel/v1';
  #csrfHeaderName = 'X-XSRF-TOKEN';

  constructor(private http: HttpClient,
    @Inject(SharedConfigService) private configuration: Configuration,
    private loadingService: LoadingIndicatorService) { }

  loadPage(suchfilter: Suchfilter | undefined, pageDefinition: PageDefinition): Observable<RaetselsucheTreffer> {

    if (suchfilter === undefined) {
      return of({trefferGesamt: 0, treffer: []});
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
    return this.loadingService.showLoaderUntilCompleted(this.http.get<RaetselsucheTreffer>(this.#url, { headers, params }));
  }

  findById(uuid: string): Observable<RaetselDetails> {

    const url = this.#url + '/' + uuid;
    const headers = new HttpHeaders().set('Accept', 'application/json');
    return this.loadingService.showLoaderUntilCompleted(this.http.get<RaetselDetails>(url, { headers: headers }));
  }

  // loadRaetselPNGs(raetselId: string): Observable<GeneratedImages> {
  //   const url = this.#url + '/PNG/' + raetselId;
  // }

  generateRaetselPNGs(raetselId: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): Observable<GeneratedImages> {

    const url = this.#url + '/PNG/' + raetselId;

    const headers = new HttpHeaders().set('Accept', 'application/json');

    return this.loadingService.showLoaderUntilCompleted(this.http.post<GeneratedImages>(url, layoutAntwortvorschlaege, { headers: headers }));
  }

  generateRaetselPDF(raetselId: string, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): Observable<GeneratedPDF> {

    const url = this.#url + '/PDF/' + raetselId;

    const headers = new HttpHeaders().set('Accept', 'application/json');
    const params = new HttpParams().set('layoutAntwortvorschlaege', layoutAntwortvorschlaege);

    return this.loadingService.showLoaderUntilCompleted(this.http.get<GeneratedPDF>(url, { headers: headers, params: params }));
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

    return this.loadingService.showLoaderUntilCompleted(this.http.put<RaetselDetails>(this.#url, editRaetselPayload, { headers }));
  }
}
