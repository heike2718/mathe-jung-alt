import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from "@angular/common/http";
import {
    AufgabensammlungenTreffer,
    AufgabensammlungenSuchparameter,
    AufgabensammlungDetails,
    EditAufgabensammlungselementPayload,
    Aufgabensammlungselement,
    EditAufgabensammlungPayload,
    AufgabensammlungBasisdaten
} from "@mja-ws/raetselgruppen/model";
import { Observable } from "rxjs";
import { map, switchMap } from 'rxjs/operators';
import { FONT_NAME, GeneratedFile, LATEX_LAYOUT_ANTWORTVORSCHLAEGE, PageDefinition, QUERY_PARAM_LIMIT, QUERY_PARAM_OFFSET, QUERY_PARAM_SORT_ATTRIBUTE, QUERY_PARAM_SORT_DIRECTION, SCHRIFTGROESSE } from "@mja-ws/core/model";
import { generateUUID } from "@mja-ws/shared/util";


@Injectable({ providedIn: 'root' })
export class RaetselgruppenHttpService {

    #http = inject(HttpClient);
    #url = '/mja-api/aufgabensammlungen';

    findRaetselgruppen(suchparameter: AufgabensammlungenSuchparameter, pageDefinition: PageDefinition): Observable<AufgabensammlungenTreffer> {

        const offset = pageDefinition.pageIndex * pageDefinition.pageSize;

        let params = new HttpParams()
            .set(QUERY_PARAM_LIMIT, pageDefinition.pageSize)
            .set(QUERY_PARAM_OFFSET, offset)
            .set(QUERY_PARAM_SORT_DIRECTION, pageDefinition.sortDirection)
            .set(QUERY_PARAM_SORT_ATTRIBUTE, suchparameter.sortAttribute);

        if (suchparameter.name && suchparameter.name.trim().length > 0) {
            params = params.set('name', suchparameter.name.trim());
        }

        if (suchparameter.schwierigkeitsgrad) {
            params = params.set('schwierigkeitsgrad', suchparameter.schwierigkeitsgrad);
        }

        if (suchparameter.referenztyp) {
            params = params.set('referenztyp', suchparameter.referenztyp);
        }

        if (suchparameter.referenz && suchparameter.referenz.trim().length > 0) {
            params = params.set('referenz', suchparameter.referenz.trim());
        }

        const url = this.#url + '/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<AufgabensammlungenTreffer>(url, { headers, params });
    }

    findById(uuid: string): Observable<AufgabensammlungDetails> {
        const headers = new HttpHeaders().set('Accept', 'application/json');
        const url = this.#url + '/' + uuid + '/v1';
        return this.#http.get<AufgabensammlungDetails>(url, { headers });
    }

    saveRaetselgruppe(EditAufgabensammlungPayload: EditAufgabensammlungPayload): Observable<AufgabensammlungBasisdaten> {

        const url = this.#url + '/v1';

        if ('neu' === EditAufgabensammlungPayload.id) {
            return this.#insertRaetselgruppe(url, EditAufgabensammlungPayload);
        } else {
            return this.#updateRaetselgruppe(url, EditAufgabensammlungPayload);
        }
    }

    saveAufgabensammlungselement(raetselgruppeID: string, payload: EditAufgabensammlungselementPayload): Observable<AufgabensammlungDetails> {

        const url = this.#url + '/' + raetselgruppeID + '/elemente/v1';

        if (payload.id === 'neu') {
            return this.#insertAufgabensammlungselement(url, payload);
        } else {
            return this.#updateAufgabensammlungselement(url, payload);
        }
    }

    deleteAufgabensammlungselement(raetselgruppeID: string, payload: Aufgabensammlungselement): Observable<AufgabensammlungDetails> {

        const url = this.#url + '/' + raetselgruppeID + '/elemente/' + payload.id + '/v1';

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.delete<AufgabensammlungDetails>(url, { headers });
    }

    generiereVorschau(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): Observable<GeneratedFile> {


        const url = this.#url + '/' + raetselgruppeID + '/vorschau/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');
        const params = new HttpParams()
            .set('font', font)
            .set('size', schriftgroesse)
            .set('layoutAntwortvorschlaege', layoutAntwortvorschlaege);


        return this.#http.get<GeneratedFile>(url, { headers: headers, params: params });

    }

    generiereKnobelkartei(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): Observable<GeneratedFile> {


        const url = this.#url + '/' + raetselgruppeID + '/knobelkartei/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');
        const params = new HttpParams()
            .set('font', font)
            .set('size', schriftgroesse)
            .set('layoutAntwortvorschlaege', layoutAntwortvorschlaege);


        return this.#http.get<GeneratedFile>(url, { headers: headers, params: params });

    }

    generiereArbeitsblattMitLoesungen(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): Observable<GeneratedFile> {


        const url = this.#url + '/' + raetselgruppeID + '/arbeitsblatt/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');
        const params = new HttpParams()
            .set('font', font)
            .set('size', schriftgroesse)
            .set('layoutAntwortvorschlaege', layoutAntwortvorschlaege);


        return this.#http.get<GeneratedFile>(url, { headers: headers, params: params });

    }

    generiereLaTeX(raetselgruppeID: string, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): Observable<{ data: Blob, fileName: string }> {

        const url = this.#url + '/' + raetselgruppeID + '/latex/v1';
        const headers = new HttpHeaders().set('Accept', 'application/octet-stream');
        const params = new HttpParams()
            .set('font', font)
            .set('size', schriftgroesse)
            .set('layoutAntwortvorschlaege', layoutAntwortvorschlaege);

        const obs$ = this.#http.get(url, { headers: headers, params: params, responseType: 'blob', observe: 'response' });

        return obs$.pipe(
            map(response => {
                const contentDispositionHeader = response.headers.get('Content-Disposition');
                const fileName = this.#extractFileName(contentDispositionHeader);
                return { data: response.body as Blob, fileName: fileName };
            })
        );
    }

    #insertRaetselgruppe(url: string, payload: EditAufgabensammlungPayload): Observable<AufgabensammlungBasisdaten> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.post<AufgabensammlungBasisdaten>(url, payload, { headers });

    }

    #updateRaetselgruppe(url: string, payload: EditAufgabensammlungPayload): Observable<AufgabensammlungBasisdaten> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.put<AufgabensammlungBasisdaten>(url, payload, { headers });

    }

    #insertAufgabensammlungselement(url: string, payload: EditAufgabensammlungselementPayload): Observable<AufgabensammlungDetails> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.post<AufgabensammlungDetails>(url, payload, { headers });
    }

    #updateAufgabensammlungselement(url: string, payload: EditAufgabensammlungselementPayload): Observable<AufgabensammlungDetails> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.put<AufgabensammlungDetails>(url, payload, { headers });
    }

    #extractFileName(contentDisposition: string | null): string {

        if (contentDisposition === null) {
            return generateUUID().substring(0, 8) + '.zip';
        }

        const tokens: string[] = contentDisposition.split(';');
        if (tokens.length === 2 && tokens[1].startsWith('filename=')) {
            return tokens[1].substring('filename='.length, tokens[1].length);
        }

        return generateUUID().substring(0, 8) + '.zip';
    }
}