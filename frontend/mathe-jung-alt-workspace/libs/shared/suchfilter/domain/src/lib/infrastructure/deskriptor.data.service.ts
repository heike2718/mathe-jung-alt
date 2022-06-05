import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Configuration, SharedConfigService } from '@mathe-jung-alt-workspace/shared/configuration';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';

@Injectable({ providedIn: 'root' })
export class DeskriptorDataService {

  #url = this.configuration.admin ? this.configuration.baseUrl + '/admin/deskriptoren/v1' : this.configuration.baseUrl + '/deskriptoren/v1';

  constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

  load(): Observable<Deskriptor[]> {

    const params = new HttpParams().set('kontext', this.configuration.admin ? 'NOOP' : 'RAETSEL');

    const headers = new HttpHeaders().set('Accept', 'application/json');
    return this.http.get<Deskriptor[]>(this.#url, { headers, params });
  }
}
