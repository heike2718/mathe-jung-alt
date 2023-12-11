import { DeskriptorUI, GeneratedImages, noopQuelle, QuelleUI } from "@mja-ws/core/model";
import { EmbeddableImageInfo } from "@mja-ws/embeddable-images/model";

export type MODUS_VOLLTEXTSUCHE = 'UNION' | 'INTERSECTION';
export type MODUS_SUCHE_MIT_DESKRIPTOREN = 'LIKE' | 'NOT_LIKE';

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

export interface RaetselDetails {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly freigegeben: boolean;
  readonly schreibgeschuetzt: boolean;
  readonly frage: string;
  readonly loesung: string | undefined;
  readonly kommentar: string | undefined;
  readonly quelle: QuelleUI;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: DeskriptorUI[];
  readonly images: GeneratedImages | null;
  readonly raetselPDF: Blob | null;
  readonly embeddableImageInfos: EmbeddableImageInfo[];
};

export interface EditRaetselPayload {
  readonly latexHistorisieren: boolean;
  readonly raetsel: RaetselDetails;
};

export const initialRaetselDetails: RaetselDetails = {
  id: 'neu',
  schluessel: '',
  name: '',
  freigegeben: false,
  schreibgeschuetzt: true,
  frage: '',
  loesung: '',
  kommentar: '',
  quelle: noopQuelle,
  antwortvorschlaege: [],
  deskriptoren: [],
  images: null,
  raetselPDF: null,
  embeddableImageInfos: []
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
