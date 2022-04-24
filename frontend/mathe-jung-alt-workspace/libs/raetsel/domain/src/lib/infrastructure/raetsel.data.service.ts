import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Raetsel, RaetselDetails } from '../entities/raetsel';
import { Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';

@Injectable({ providedIn: 'root' })
export class RaetselDataService {


  #alleRaetsel: Raetsel[] = [{
    id: 'uuid-1',
    schluessel: '00001',
    name: 'Spielsteine umschichten',
    deskriptoren: ['Minikänguru', 'ZWEI-A', 'EINS-B'],
  },
  {
    id: 'uuid-2',
    schluessel: '00002',
    name: 'verschachteltes Polynom auflösen',
    deskriptoren: ['Serie', 'ab Klasse 9']
  },
  {
    id: 'uuid-3',
    schluessel: '00003',
    name: 'Frühlingsbeet',
    deskriptoren: ['Serie', 'Klassen 1/2']
  },
  {
    id: 'uuid-4',
    schluessel: '00004',
    name: 'Klassen und Fächer',
    deskriptoren: ['Serie', 'Klassen 3/4']
  },
  {
    id: 'uuid-5',
    schluessel: '00005',
    name: 'Rundreise',
    deskriptoren: ['Serie', 'Klassen 1/2']
  },
  {
    id: 'uuid-6',
    schluessel: '00006',
    name: 'Kryptogramm mit Multiplikation',
    deskriptoren: ['Serie', 'Klassen 3/4']
  },
  {
    id: 'uuid-7',
    schluessel: '00007',
    name: 'Geheimcode',
    deskriptoren: ['Serie', 'Klassen 5/6']
  },
  {
    id: 'uuid-8',
    schluessel: '00008',
    name: 'Regenwetter',
    deskriptoren: ['Serie', 'ab Klasse 9']
  },
  {
    id: 'uuid-9',
    schluessel: '00009',
    name: 'Primzahlsuche',
    deskriptoren: ['Serie', 'ab Klasse 9']
  }, {
    id: 'uuid-10',
    schluessel: '00010',
    name: 'Klassenfahrt',
    deskriptoren: ['Serie', 'ab Klasse 9']
  },
  {
    id: 'uuid-11',
    schluessel: '00011',
    name: 'Spielgeräte zuordnen',
    deskriptoren: ['Serie', 'Vorschule']
  },
  {
    id: 'uuid-12',
    schluessel: '00012',
    name: 'zerbrochene Fensterscheiben',
    deskriptoren: ['Serie', 'Vorschule']
  },
  {
    id: 'uuid-13',
    schluessel: '00013',
    name: 'Adventsbasar',
    deskriptoren: ['Serie', 'Klassen 3/4']
  },
  {
    id: 'uuid-14',
    schluessel: '00014',
    name: 'Tiere und Beine',
    deskriptoren: ['Serie', 'Klassen 1/2']
  },
  {
    id: 'uuid-15',
    schluessel: '00015',
    name: 'Hasenschule',
    deskriptoren: ['Serie', 'Vorschule']
  },
  {
    id: 'uuid-16',
    schluessel: '00016',
    name: 'Wettlauf',
    deskriptoren: ['Serie', 'Klassen 1/2']
  },
  {
    id: 'uuid-17',
    schluessel: '00017',
    name: 'Kongruente Dreiecke finden',
    deskriptoren: ['Serie', 'Klassen 5/6']
  },
  {
    id: 'uuid-18',
    schluessel: '00018',
    name: 'Labyrinth mit Kaninchen',
    deskriptoren: ['Serie', 'Vorschule']
  },
  {
    id: 'uuid-19',
    schluessel: '00019',
    name: 'Diophantische Gleichung',
    deskriptoren: ['Serie', 'Klassen 7/8']
  }];

  #deskriptoren: string[] = [];

  constructor(private http: HttpClient) {

    for (let i = 0; i < this.#alleRaetsel.length; i++) {
      const raetsel: Raetsel = this.#alleRaetsel[i];

      const deskr: string[] = raetsel.deskriptoren;
      for (let j = 0; j < deskr.length; j++) {
        const d = deskr[j];
        if (!this.#deskriptoren.includes(d)) {
          this.#deskriptoren.push(d);
        }
      }
    }
  }

  load(): Observable<Raetsel[]> {
    // Uncomment if needed
    /*
        const url = '...';
        const params = new HttpParams().set('param', 'value');
        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.http.get<Raetsel[]>(url, {params, headers});
        */

    return of(this.#alleRaetsel);
  }

  findRaetsel(suchfilter: Suchfilter): Observable<Raetsel[]> {

    if (suchfilter.suchstring && suchfilter.suchstring.trim().length > 0) {
      const filtered = this.#alleRaetsel.filter(raetsel => raetsel.name.toLocaleLowerCase().includes(suchfilter.suchstring.trim().toLocaleLowerCase()));
      return of(filtered);
    }
    return of(this.#alleRaetsel);
  }

  loadPage(filter: string, sortDirection: string, pageIndex: number, pageSize: number): Observable<Raetsel[]> {

    const first = pageIndex * pageSize;

    let filtered: Raetsel[] = this.#alleRaetsel;

    if (filter && filter.trim().length > 0) {
      filtered = this.#alleRaetsel.filter(raetsel => raetsel.name.toLocaleLowerCase().includes(filter.trim().toLocaleLowerCase()));
    }

    let result: Raetsel[] = filtered.slice(first, first + pageSize);

    if (sortDirection === 'desc') {
      result = result.sort((r1, r2) => r2.schluessel.localeCompare(r1.schluessel));
    }

    return of(result);
  }

  countRaetsel(filter: string): Observable<number> {

    if (!filter || filter.trim().length === 0) {
      return of(0);
    }

    const treffer: Raetsel[] = this.#alleRaetsel.filter(r => r.name.toLocaleLowerCase().includes(filter.trim().toLocaleLowerCase()));

    return of(treffer.length);


  }

  anzahlRaetsel(): number {
    return this.#alleRaetsel.length;
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
      deskriptoren: ['Serie', 'ab Klasse 9']
    })
  }
}


