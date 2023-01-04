import { DeskriptorUI, GeneratedImages, STATUS } from "@mja-ws/core/model";

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
  readonly loesung?: string;
  readonly kommentar?: string;
  readonly quelleId: string;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: DeskriptorUI[];
  readonly images: GeneratedImages | null;
  readonly raetselPDF: Blob | null;
  readonly grafikInfos: GrafikInfo[];
};

export interface RaetselDetailsContent {
  readonly raetsel: RaetselDetails;
  readonly quelleId?: string;
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
  quelleId: '',
  antwortvorschlaege: [],
  deskriptoren: [],
  images: null,
  raetselPDF: null,
  grafikInfos: []  
};

export interface SelectableDeskriptoren {
  readonly deskriptor: DeskriptorUI;
  readonly selected: boolean;
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

