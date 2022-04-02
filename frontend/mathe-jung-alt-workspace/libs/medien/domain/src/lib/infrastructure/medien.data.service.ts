import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Medium } from '../entities/medien';

@Injectable({ providedIn: 'root' })
export class MedienDataService {
  constructor(private http: HttpClient) {}

  load(): Observable<Medium[]> {
    // Uncomment if needed
    /*
        const url = '...';
        const params = new HttpParams().set('param', 'value');
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.http.get<Medien[]>(url, {params, headers});
        */

    return of([
      { uuid: '80e5ffbe', 
        name: 'Kiewer Matheolympiaden 9-10',
        art: 'BUCH',
        uri: 'file:///media/veracrypt2/verzeichnis1/datei1.pdf'
      },
      { uuid: 'fe31aa52', 
        name: 'alpha',
        art: 'ZEITSCHRIFT',
        uri: 'file:///media/veracrypt2/zeitschriften/alpha'
      },
    ]);
  }
}
