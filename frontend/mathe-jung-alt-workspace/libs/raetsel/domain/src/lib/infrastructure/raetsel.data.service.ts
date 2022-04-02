import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Raetsel, RaetselDetails } from '../entities/raetsel';

@Injectable({ providedIn: 'root' })
export class RaetselDataService {

  constructor(private http: HttpClient) { }

  load(): Observable<Raetsel[]> {
    // Uncomment if needed
    /*
        const url = '...';
        const params = new HttpParams().set('param', 'value');
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.http.get<Raetsel[]>(url, {params, headers});
        */

    return of([
      {
        id: '7c4962f0',
        schluessel: '00001',
        name: 'Spielsteine umschichten',
        deskriptoren: ['Minikänguru', 'ZWEI-A', 'EINS-B'],
      },
      {
        id: '52d631a7',
        schluessel: '00002',
        name: 'verschachteltes Polynom auflösen',
        deskriptoren: ['Serie', 'Stufe 6']
      }
    ]);
  }

  findById(uuid: string): Observable<RaetselDetails> {

    if (uuid === '7c4962f0') {

      return of(
        {
          id: '7c4962f0',
          schluessel: '00001',
          name: 'Spielsteine umschichten',
          text: 'Das Känguru schichtet Spielsteine um \par An welcher liegt nun Stein 5?',
          ursprung: 'NACHBAU',
          primaerquelleId: '5ec5aed7',
          sekundaerquelleId: '60cfd0da',
          deskriptoren: ['Minikänguru', 'ZWEI-A', 'EINS-B'],
          antwortvorschlaege: [
            {
              buchstabe: 'A',
              text: '13',
              loesung: false
            },
            {
              buchstabe: 'B',
              text: '23',
              loesung: false
            },
            {
              buchstabe: 'C',
              text: '20',
              loesung: false
            },
            {
              buchstabe: 'D',
              text: '12',
              loesung: true
            },
            {
              buchstabe: 'E',
              text: '9',
              loesung: false
            }
  
          ],        
        }
      );
    }

    return of({
      id: '52d631a7',
        schluessel: '00002',
        name: 'verschachteltes Polynom auflösen',
        text: '\[ f(x) = \frac{1}{2} x^2 \]',
        ursprung: 'ZITAT',
        primaerquelleId: '0521545a',
        deskriptoren: ['Serie', 'Stufe 6']
    })
  }
}
