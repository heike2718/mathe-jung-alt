import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Deskriptor } from '../entities/deskriptor';
import { Configuration, SharedConfigService } from '@mathe-jung-alt-workspace/shared/configuration';
import { Suchkontext } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';

@Injectable({ providedIn: 'root' })
export class DeskriptorDataService {

  #url = this.configuration.admin ? this.configuration.baseUrl + '/admin/deskriptoren' : this.configuration.baseUrl + '/deskriptoren';

  constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

  load(kontext: Suchkontext): Observable<Deskriptor[]> {

    // const params = new HttpParams().set('param', 'value');

    const urlWithQuery = this.#url + '?kontext=' + kontext;

    const headers = new HttpHeaders().set('Accept', 'application/json');
    return this.http.get<Deskriptor[]>(urlWithQuery, { headers });
  }
}
