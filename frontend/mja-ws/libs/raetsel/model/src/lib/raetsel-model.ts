import { DeskriptorUI, GeneratedImages, noopQuelle, QuelleUI, STATUS } from "@mja-ws/core/model";
import { Message } from "@mja-ws/shared/messaging/api";

export interface RaetselSuchfilter {
  readonly suchstring: string;
  readonly deskriptoren: DeskriptorUI[];
};

export const initialRaetselSuchfilter: RaetselSuchfilter = {
  suchstring: '',
  deskriptoren: []
};

export interface Antwortvorschlag {
  readonly buchstabe: string;
  readonly text?: string;
  readonly korrekt: boolean;
};

export interface RaetselsucheTreffer {
  readonly trefferGesamt: number;
  readonly treffer: Raetsel[];
};

export interface GrafikInfo {
  readonly pfad: string;
  readonly existiert: boolean;
};

export interface GrafikSearchResult {
  readonly pfad: string;
  readonly messagePayload: Message;
  readonly image?: string;
};

export const nullGraphicSearchResult: GrafikSearchResult = {pfad: '', messagePayload: {level: 'INFO', message: ''}};



/** Minimalset an Attributen, die bei einer Suche geladen werden sollen */
export interface Raetsel {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly status: STATUS;
  readonly kommentar?: string;
  readonly deskriptoren: DeskriptorUI[];
};

export interface RaetselDetails {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly status: STATUS;
  readonly schreibgeschuetzt: boolean;
  readonly frage: string;
  readonly loesung: string | undefined;
  readonly kommentar: string | undefined;
  readonly quelle: QuelleUI;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: DeskriptorUI[];
  readonly images: GeneratedImages | null;
  readonly raetselPDF: Blob | null;
  readonly grafikInfos: GrafikInfo[];
};

export interface EditRaetselPayload {
  readonly latexHistorisieren: boolean;
  readonly raetsel: RaetselDetails;
};

export const initialRaetselDetails: RaetselDetails = {
  id: 'neu',
  schluessel: '',
  name: '',
  status: 'ERFASST',
  schreibgeschuetzt: true,
  frage: '',
  loesung: '',
  kommentar: '',
  quelle: noopQuelle,
  antwortvorschlaege: [],
  deskriptoren: [],
  images: null,
  raetselPDF: null,
  grafikInfos: []  
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

