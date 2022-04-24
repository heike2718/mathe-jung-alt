import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Deskriptor } from '../entities/deskriptor';

@Injectable({ providedIn: 'root' })
export class DeskriptorDataService {
  constructor(private http: HttpClient) {}

  load(): Observable<Deskriptor[]> {
    // Uncomment if needed
    /*
        const url = '...';
        const params = new HttpParams().set('param', 'value');
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.http.get<Deskriptor[]>(url, {params, headers});
        */

    return of([
      { id: 1, 
        name: 'Minikänguru' 
      },
      {
        id: 2,
        name: 'EINS-B',
      },
      {
        id: 3,
        name: 'ZWEI-A',
      },
      {
        id: 4,
        name: 'Serie',
      },
      {
        id: 5,
        name: 'Vorschule',
      },
      {
        id: 6,
        name: 'Klassen 1/2',
      },
      {
        id: 7,
        name: 'Klassen 3/4',
      },
      {
        id: 8,
        name: 'Klassen 5/6',
      },
      {
        id: 9,
        name: 'Klassen 7/8',
      },
      {
        id: 10,
        name: 'ab Klasse 9',
      },
    ]);
  }
}
