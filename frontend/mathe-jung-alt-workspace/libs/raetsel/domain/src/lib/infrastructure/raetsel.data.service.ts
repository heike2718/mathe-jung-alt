import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { EditRaetselPayload, Raetsel, RaetselDetails } from '../entities/raetsel';
import { PageDefinition, Suchfilter, SuchfilterQueryParameterMapper, SuchfilterWithStatus } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { Configuration, SharedConfigService } from '@mathe-jung-alt-workspace/shared/configuration';

@Injectable({ providedIn: 'root' })
export class RaetselDataService {


  #url = this.configuration.admin ? this.configuration.baseUrl + '/admin/raetsel/v1' : this.configuration.baseUrl + '/raetsel/v1';


  constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

  countRaetsel(suchfilter: Suchfilter): Observable<number> {
    if (suchfilter.deskriptoren.length === 0 && (suchfilter.suchstring === undefined || suchfilter.suchstring.trim().length === 0)) {
      return of(0);
    }

    const queryParams = new SuchfilterQueryParameterMapper(suchfilter).apply();

    const headers = new HttpHeaders().set('Accept', 'text/plain');
    const url = this.#url + '/size' + queryParams;

    return this.http.get<number>(url, { headers });
  }

  loadPage(suchfilterWithStatus: SuchfilterWithStatus, pageDefinition: PageDefinition): Observable<Raetsel[]> {

    if (!suchfilterWithStatus.nichtLeer) {
      return of([]);
    }

    const suchfilter: Suchfilter = suchfilterWithStatus.suchfilter;
    const offset = pageDefinition.pageIndex * pageDefinition.pageSize;

    const params = new HttpParams()
    .set('suchstring', suchfilter.suchstring)
    .set('deskriptoren', new SuchfilterQueryParameterMapper(suchfilter).getDeskriptoren())
    .set('limit', pageDefinition.pageSize)
    .set('offset', offset)
    .set('sortDirection', pageDefinition.sortDirection);


    const headers = new HttpHeaders().set('Accept', 'application/json');
    return this.http.get<Raetsel[]>(this.#url, {headers, params});
  }

  public saveRaetsel(editRaetselPayload: EditRaetselPayload): Observable<RaetselDetails> {

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


