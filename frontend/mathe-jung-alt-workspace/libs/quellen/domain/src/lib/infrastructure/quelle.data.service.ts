import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Quelle } from '../entities/quelle';
import { Configuration, SharedConfigService } from '@mathe-jung-alt-workspace/shared/configuration';
import { Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';

@Injectable({ providedIn: 'root' })
export class QuelleDataService {


  #url = this.configuration.admin ? this.configuration.baseUrl + '/admin/quellen' : this.configuration.baseUrl + '/quellen';

  constructor(private http: HttpClient, @Inject(SharedConfigService) private configuration: Configuration) { }

  findQuellen(suchfilter: Suchfilter): Observable<Quelle[]> {

    const headers = new HttpHeaders().set('Accept', 'application/json');
    return this.http.get<Quelle[]>(this.#url + '?suchstring=' + suchfilter.suchstring, { headers });

  }


  load(): Observable<Quelle[]> {
    // Uncomment if needed
    /*
        const url = '...';
        const params = new HttpParams().set('param', 'value');
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.http.get<Quelle[]>(url, {params, headers});
        */

    return of([
      {
        uuid: '5ec5aed7',
        quellenart: 'PERSON',
        sortNumber: 1,
        name: 'Heike Winkelvoß',
        deskriptoren: []
      },
      {
        uuid: '0521545a',
        quellenart: 'ZEITSCHRIFT',
        sortNumber: 2,
        name: 'alpha (6) 1978',
        mediumUuid: 'fe31aa52',
        deskriptoren: []
      }
    ]);
  }
}
