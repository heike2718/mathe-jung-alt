import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Quelle } from '../entities/quelle';

@Injectable({ providedIn: 'root' })
export class QuelleDataService {
  constructor(private http: HttpClient) {}

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
        art: 'PERSON',
        beschreibung: 'Heike Winkelvoß'
      },
      {
        uuid: '0521545a',
        art: 'ZEITSCHRIFT',
        mediumId: 'fe31aa52',
        beschreibung: '6(1976)'
      }      
    ]);
  }
}
