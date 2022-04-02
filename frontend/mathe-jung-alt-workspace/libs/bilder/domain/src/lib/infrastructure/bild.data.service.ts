import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Bild } from '../entities/bild';

@Injectable({ providedIn: 'root' })
export class BildDataService {
  constructor(private http: HttpClient) {}

  load(): Observable<Bild[]> {
    // Uncomment if needed
    /*
        const url = '...';
        const params = new HttpParams().set('param', 'value');
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.http.get<Bild[]>(url, {params, headers});
        */

    return of([
      {
        uuid: 'bbbdd8e9',
        dateinameEps: '00963.eps',
        dateinamePdf: '00963-eps-converted-to.pdf',
        pfad: '../resources/002/'
      }
    ]);
  }
}
