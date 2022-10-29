import { GeneratedImages } from "@mja-workspace/shared/ui-components";
import { STATUS } from "@mja-workspace/shared/util-mja";
import { Deskriptor, Suchkontext } from "@mja-workspace/suchfilter/domain";

export type LATEX_OUTPUTFORMAT = 'PDF' | 'PNG';
export type LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'ANKREUZTABELLE' | 'BUCHSTABEN' | 'DESCRIPTION' | 'NOOP';

export const anzeigeAntwortvorschlaegeSelectInput: string[] = [
  'ANKREUZTABELLE', 'BUCHSTABEN', 'DESCRIPTION', 'NOOP'
];

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
  readonly deskriptoren: Deskriptor[];
};

export interface RaetselDetails {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly status: STATUS;
  readonly frage: string;
  readonly loesung?: string;
  readonly kommentar?: string;
  readonly quelleId: string;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: Deskriptor[];
  readonly images: GeneratedImages | null;
  readonly raetselPDF: Blob | null;
  readonly grafikInfos: GrafikInfo[];
};

export interface RaetselDetailsContent {
  readonly kontext: Suchkontext;
  readonly raetsel: RaetselDetails;
  readonly quelleId?: string;
};

export interface EditRaetselPayload {
  readonly latexHistorisieren: boolean;
  readonly raetsel: RaetselDetails;
};

export interface GeneratedPDF {
  readonly url?: string,
  readonly fileName: string,
  readonly fileData: Blob
};

export const initialRaetselDetails: RaetselDetails = {
  id: 'neu',
  schluessel: '',
  name: '',
  status: 'ERFASST',
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



export const initialRaetselDetailsContent: RaetselDetailsContent = {
  kontext: "RAETSEL",
  raetsel: initialRaetselDetails
};

