import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Raetsel, RaetselDetails } from '../entities/raetsel';
import { Suchfilter } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';

@Injectable({ providedIn: 'root' })
export class RaetselDataService {


  #alleRaetsel: Raetsel[] = [{
    id: 'uuid-1',
    schluessel: '00001',
    name: 'Spielsteine umschichten',
    deskriptoren: [{'id':2,'name':"Minikänguru",'admin':false,'kontext':'RAETSEL'},
    {"id":7,"name":"EINS","admin":false,"kontext":"RAETSEL"},
    {"id":8,"name":"ZWEI","admin":false,"kontext":"RAETSEL"},
    {"id":9,"name":"A","admin":false,"kontext":"RAETSEL"},
    {"id":10,"name":"B","admin":false,"kontext":"RAETSEL"}],
  },{
    id: 'uuid-2',
    schluessel: '00002',
    name: 'verschachteltes Polynom auflösen',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":17,"name":"ab Klasse 9","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-3',
    schluessel: '00003',
    name: 'Frühlingsbeet',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":13,"name":"Klassen 1/2","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-4',
    schluessel: '00004',
    name: 'Klassen und Fächer',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":14,"name":"Klassen 3/4","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-5',
    schluessel: '00005',
    name: 'Rundreise',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":13,"name":"Klassen 1/2","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-6',
    schluessel: '00006',
    name: 'Kryptogramm mit Multiplikation',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":14,"name":"Klassen 3/4","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-7',
    schluessel: '00007',
    name: 'Geheimcode',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":15,"name":"Klassen 5/6","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-8',
    schluessel: '00008',
    name: 'Regenwetter',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":17,"name":"ab Klasse 9","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-9',
    schluessel: '00009',
    name: 'Primzahlsuche',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":17,"name":"ab Klasse 9","admin":false,"kontext":"RAETSEL"}]
  }, {
    id: 'uuid-10',
    schluessel: '00010',
    name: 'Klassenfahrt',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":17,"name":"ab Klasse 9","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-11',
    schluessel: '00011',
    name: 'Spielgeräte zuordnen',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":12,"name":"Vorschule","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-12',
    schluessel: '00012',
    name: 'zerbrochene Fensterscheiben',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":12,"name":"Vorschule","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-13',
    schluessel: '00013',
    name: 'Adventsbasar',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":14,"name":"Klassen 3/4","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-14',
    schluessel: '00014',
    name: 'Tiere und Beine',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":13,"name":"Klassen 1/2","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-15',
    schluessel: '00015',
    name: 'Hasenschule',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":12,"name":"Vorschule","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-16',
    schluessel: '00016',
    name: 'Wettlauf',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":13,"name":"Klassen 1/2","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-17',
    schluessel: '00017',
    name: 'Kongruente Dreiecke finden',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, {"id":15,"name":"Klassen 5/6","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-18',
    schluessel: '00018',
    name: 'Labyrinth mit Kaninchen',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, 
    {"id":12,"name":"Vorschule","admin":false,"kontext":"RAETSEL"}]
  },
  {
    id: 'uuid-19',
    schluessel: '00019',
    name: 'Diophantische Gleichung',
    deskriptoren: [{"id":18,"name":"Serie","admin":false,"kontext":"RAETSEL"}, 
    {"id":16,"name":"Klassen 7/8","admin":false,"kontext":"RAETSEL"}]
  }
  ];

  #deskriptoren: Deskriptor[] = [];

  constructor(private http: HttpClient) {

    for (let i = 0; i < this.#alleRaetsel.length; i++) {
      const raetsel: Raetsel = this.#alleRaetsel[i];

      const deskr: Deskriptor[] = raetsel.deskriptoren;
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

    if (suchfilter.suchstring.trim().length > 0) {
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

  public findById(uuid: string): Observable<RaetselDetails> {

    return of({
      "id": "7a94e100-85e9-4ffb-903b-06835851063b",
      "schluessel": "02789",
      "name": "2022 zählen",
      "frage": "\\begin{center}{\\Large \\bf 2 0 2 2 2 0 2 2 2 0 2 2 2 0 2 2 2 0 2 2 0 0 2 2 2 0 2 2 2 0 2 2 2 0 2 2}\\end{center}Wie oft steht hier 2022?",
      "loesung": "alle 2022 umranden, dann zählen",
      "kommentar": "Minikänguru 2022 Klasse 1",
      "quelleId": "8ef4d9b8-62a6-4643-8674-73ebaec52d98",
      "antwortvorschlaege": [
        {
          "buchstabe": "A",
          "text": "7 Mal",
          "korrekt": false
        },
        {
          "buchstabe": "B",
          "text": "9 Mal",
          "korrekt": false
        },
        {
          "buchstabe": "C",
          "text": "8 Mal",
          "korrekt": true
        },
        {
          "buchstabe": "D",
          "text": "10 Mal",
          "korrekt": false
        },
        {
          "buchstabe": "E",
          "text": "11 Mal",
          "korrekt": false
        }
      ],
      "deskriptoren": [
        {
          "id": 2,
          "name": "Minikänguru",
          "admin": true,
          "kontext": "RAETSEL"
        },
        {
          "id": 4,
          "name": "Klasse 1",
          "admin": false,
          "kontext": "RAETSEL"
        },
        {
          "id": 7,
          "name": "EINS",
          "admin": true,
          "kontext": "RAETSEL"
        },
        {
          "id": 9,
          "name": "A",
          "admin": true,
          "kontext": "RAETSEL"
        },
        {
          "id": 46,
          "name": "A-1",
          "admin": true,
          "kontext": "RAETSEL"
        }
      ],
      "imageFrage": "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",
      "imageLoesung": null
    });
  }
}


