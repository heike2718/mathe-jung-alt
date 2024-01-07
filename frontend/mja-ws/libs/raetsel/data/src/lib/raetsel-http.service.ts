import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import {
    DeskriptorUI,
    FontName,
    GeneratedFile,
    GeneratedImages,
    LaTeXLayoutAntwortvorschlaege,
    Medienart,
    PageDefinition,
    QUERY_PARAM_DESKRIPTOREN,
    QUERY_PARAM_LIMIT,
    QUERY_PARAM_MODE_FULLTEXT_SEARCH,
    QUERY_PARAM_OFFSET,
    QUERY_PARAM_SEARCH_MODE_FOR_DESCRIPTORS,
    QUERY_PARAM_SORT_DIRECTION,
    QUERY_PARAM_SUCHSTRING,
    QUERY_PARAM_TYPE_DESKRIPTOREN,
    Schriftgroesse
} from "@mja-ws/core/model";
import { EditRaetselPayload, RaetselDetails, RaetselsucheTreffer, RaetselSuchfilter, MediumQuelleDto, LinkedAufgabensammlung } from "@mja-ws/raetsel/model";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class RaetselHttpService {

    #http = inject(HttpClient);

    #url = '/mja-api/raetsel';

    findRaetsel(admin: boolean, suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition): Observable<RaetselsucheTreffer> {

        const offset = pageDefinition.pageIndex * pageDefinition.pageSize;

        const modusVolltextsuche: string = suchfilter.modeFullTextSearch === 'UNION' ? 'UNION' : 'INTERSECTION';
        const suchmodusFuerDeskriptoren: string = suchfilter.searchModeForDescriptors === 'LIKE' ? 'LIKE' : 'NOT_LIKE';

        let params = new HttpParams()
            .set(QUERY_PARAM_MODE_FULLTEXT_SEARCH, modusVolltextsuche)
            .set(QUERY_PARAM_SEARCH_MODE_FOR_DESCRIPTORS, suchmodusFuerDeskriptoren)
            .set(QUERY_PARAM_LIMIT, pageDefinition.pageSize)
            .set(QUERY_PARAM_OFFSET, offset)
            .set(QUERY_PARAM_SORT_DIRECTION, pageDefinition.sortDirection);

        if (suchfilter.suchstring && suchfilter.suchstring.trim().length > 0) {
            params = params.set(QUERY_PARAM_SUCHSTRING, suchfilter.suchstring.trim());
        }

        if (suchfilter.deskriptoren.length > 0) {
            params = params.set(QUERY_PARAM_DESKRIPTOREN, this.#getDeskriptoren(suchfilter));
        }

        params = params.set(QUERY_PARAM_TYPE_DESKRIPTOREN, 'ORDINAL');

        let url = this.#url + '/v2';

        if ( admin) {
            url = this.#url + '/admin/v2';
        }

        const headers = new HttpHeaders().set('Accept', 'application/json');
        return this.#http.get<RaetselsucheTreffer>(url, { headers, params });
    }

    loadRaetselDetails(schluessel: string): Observable<RaetselDetails> {

        const url = this.#url + '/' + schluessel + '/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<RaetselDetails>(url, { headers: headers });
    }

    generateRaetselPNGs(raetselId: string, font: FontName, schriftgroesse: Schriftgroesse, layoutAntwortvorschlaege: LaTeXLayoutAntwortvorschlaege): Observable<GeneratedImages> {

        const url = this.#url + '/PNG/' + raetselId + '/v1';

        const headers = new HttpHeaders().set('Accept', 'application/json');
        const params = new HttpParams()
            .set('font', font)
            .set('layoutAntwortvorschlaege', layoutAntwortvorschlaege)
            .set('size', schriftgroesse);

        return this.#http.post<GeneratedImages>(url, layoutAntwortvorschlaege, { headers: headers, params: params });
    }

    generateRaetselPDF(raetselId: string, font: FontName, schriftgroesse: Schriftgroesse, layoutAntwortvorschlaege: LaTeXLayoutAntwortvorschlaege): Observable<GeneratedFile> {

        const url = this.#url + '/PDF/' + raetselId + '/v1';

        const headers = new HttpHeaders().set('Accept', 'application/json');
        const params = new HttpParams()
            .set('font', font)
            .set('layoutAntwortvorschlaege', layoutAntwortvorschlaege)
            .set('size', schriftgroesse);

        return this.#http.get<GeneratedFile>(url, { headers: headers, params: params });
    }

    downloadLatexLogs(schluessel: string): Observable<GeneratedFile[]> {

        const url = this.#url + '/latexlogs/' + schluessel + '/v1';

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<GeneratedFile[]>(url, { headers: headers });
    }

    downloadEmbeddedImages(raetselID: string): Observable<GeneratedFile[]> {

        const url = this.#url + '/embedded-images/' + raetselID + '/v1';

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<GeneratedFile[]>(url, { headers: headers });
    }

    downloadRaetselLatex(raetselID: string): Observable<GeneratedFile[]> {

        const url = this.#url + '/raetsel-texte/' + raetselID + '/v1';

        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<GeneratedFile[]>(url, { headers: headers });
    }


    saveRaetsel(editRaetselPayload: EditRaetselPayload): Observable<RaetselDetails> {

        const url = this.#url + '/v1';

        const headers = new HttpHeaders().set('Accept', 'application/json');

        if ('neu' === editRaetselPayload.id) {
            return this.#http.post<RaetselDetails>(url, editRaetselPayload, { headers });
        }

        return this.#http.put<RaetselDetails>(url, editRaetselPayload, { headers })
    }

    findByMedienart(medienart: Medienart): Observable<MediumQuelleDto[]> {

        const headers = new HttpHeaders().set('Accept', 'application/json');
        const url = '/mja-api/medien/quelle/v1';
        let theMedienartStr: string = '';
        switch(medienart) {
            case 'BUCH': theMedienartStr = 'BUCH'; break;
            case 'INTERNET': theMedienartStr = 'INTERNET'; break;
            case 'ZEITSCHRIFT': theMedienartStr = 'ZEITSCHRIFT'; break;
        }

        const params = new HttpParams()
            .set('medienart', theMedienartStr);

        return this.#http.get<MediumQuelleDto[]>(url, { headers, params });
    }

    findLinkedAufgabensammlungen(raetselId: string): Observable<LinkedAufgabensammlung[]> {

        const url = this.#url + '/' + raetselId + '/aufgabensammlungen/v1';
        const headers = new HttpHeaders().set('Accept', 'application/json');

        return this.#http.get<LinkedAufgabensammlung[]>(url, { headers: headers });
    }


    #getDeskriptoren(suchfilter: RaetselSuchfilter): string {

        let result = '';
        for (let index = 0; index < suchfilter.deskriptoren.length; index++) {

            const deskriptor: DeskriptorUI = suchfilter.deskriptoren[index];

            if (index < suchfilter.deskriptoren.length - 1) {
                result += deskriptor.id + ',';
            } else {
                result += deskriptor.id
            }
        }
        return result;
    }
}