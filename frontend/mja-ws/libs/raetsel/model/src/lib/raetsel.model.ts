import { DeskriptorUI, GeneratedImages, HerkunftRaetsel, Herkunftstyp, Quellenart, initialHerkunftRaetsel } from "@mja-ws/core/model";
import { EmbeddableImageInfo } from "@mja-ws/embeddable-images/model";

export type MODUS_VOLLTEXTSUCHE = 'UNION' | 'INTERSECTION';
export type MODUS_SUCHE_MIT_DESKRIPTOREN = 'LIKE' | 'NOT_LIKE';

export interface QuelleDto {
  readonly id: string;
  readonly quellenart: Quellenart;
  readonly klasse: string | undefined;
  readonly stufe: string | undefined;
  readonly ausgabe: string | undefined;
  readonly jahr: string | undefined;
  readonly seite: string| undefined;
  readonly person: string | undefined;
};

export const initialQuelleDto: QuelleDto = {
  id: 'neu',
  quellenart: 'PERSON',
  ausgabe: undefined,
  jahr: undefined,
  klasse: undefined,
  person: undefined,
  seite: undefined,
  stufe: undefined
};

export interface RaetselSuchfilter {
  readonly suchstring: string;
  readonly deskriptoren: DeskriptorUI[];
  readonly modeFullTextSearch: MODUS_VOLLTEXTSUCHE;
  readonly searchModeForDescriptors: MODUS_SUCHE_MIT_DESKRIPTOREN;
};

export const initialRaetselSuchfilter: RaetselSuchfilter = {
  suchstring: '',
  deskriptoren: [],
  modeFullTextSearch: 'UNION',
  searchModeForDescriptors: 'LIKE'
};

export interface Antwortvorschlag {
  readonly buchstabe: string;
  readonly text: string | undefined;
  readonly korrekt: boolean;
};

export interface RaetselsucheTreffer {
  readonly trefferGesamt: number;
  readonly treffer: Raetsel[];
};

/** Minimalset an Attributen, die bei einer Suche geladen werden sollen */
export interface Raetsel {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly freigegeben: boolean;
  readonly kommentar: string | undefined;
  readonly deskriptoren: DeskriptorUI[];
};

/**
 * Details eines Rätsels zum Anzeigen in der Detailansicht
 */
export interface RaetselDetails {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly freigegeben: boolean;
  readonly schreibgeschuetzt: boolean;
  readonly frage: string;
  readonly loesung: string | undefined;
  readonly kommentar: string | undefined;
  readonly herkunft: HerkunftRaetsel;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: DeskriptorUI[];
  readonly images: GeneratedImages | null;
  readonly raetselPDF: Blob | null;
  readonly embeddableImageInfos: EmbeddableImageInfo[];
};

/** 
 * Payload zum Anlegen oder Ändern eines Rätsels
*/
export const initialRaetselDetails: RaetselDetails = {
  id: 'neu',
  schluessel: '',
  name: '',
  freigegeben: false,
  schreibgeschuetzt: true,
  frage: '',
  loesung: '',
  kommentar: '',
  herkunft: initialHerkunftRaetsel,
  antwortvorschlaege: [],
  deskriptoren: [],
  images: null,
  raetselPDF: null,
  embeddableImageInfos: []
};

export interface EditRaetselPayload {
  readonly id: string;
  readonly schluessel: string | null;
  readonly latexHistorisieren: boolean;
  readonly name: string;
  readonly freigegeben: boolean;
  readonly herkunftstyp: Herkunftstyp;
  readonly frage: string;
  readonly loesung: string | undefined;
  readonly kommentar: string | undefined;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: DeskriptorUI[];
  readonly quelle: QuelleDto;
};

export function deskriptorenToString(deskriptoren: DeskriptorUI[]): string {

  if (deskriptoren.length === 0) {
    return '';
  }

  let result = '';

  for (let index = 0; index < deskriptoren.length; index++) {

    const deskriptor = deskriptoren[index];
    if (index < deskriptoren.length - 1) {
      result += deskriptor.name + ", ";
    } else {
      result += deskriptor.name;
    }

  }

  return result;
};

export function isSuchfilterEmpty(suchfilter: RaetselSuchfilter): boolean {

  return suchfilter.suchstring === '' && suchfilter.deskriptoren.length === 0;


}
