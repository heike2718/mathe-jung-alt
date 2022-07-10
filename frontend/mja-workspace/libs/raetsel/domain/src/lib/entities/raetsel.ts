import { SelectableItem } from "@mja-workspace/shared/util-mja";
import { Deskriptor, Suchkontext } from "@mja-workspace/suchfilter/domain";

export type LATEX_OUTPUTFORMAT = 'PDF' | 'PNG';
export type LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'ANKREUZTABELLE' | 'BUCHSTABEN' | 'DESCRIPTION' | 'NOOP';
export type STATUS = 'ERFASST' | 'FREIGEGEBEN';

export const anzeigeAntwortvorschlaegeSelectInput: string[] = [
  'ANKREUZTABELLE', 'BUCHSTABEN', 'DESCRIPTION', 'NOOP'
];

export interface Antwortvorschlag {
  readonly buchstabe: string;
  readonly text?: string;
  readonly korrekt: boolean;
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
  readonly imageFrage: string | null;
  readonly imageLoesung: string | null;
};

export interface RaetselDetailsContent {
  readonly kontext: Suchkontext;
  readonly raetsel: RaetselDetails;
  readonly quelleId?: string;
  readonly selectableDeskriptoren: SelectableItem[];
};

export interface GeneratedImages {
  readonly outputFormat: LATEX_OUTPUTFORMAT,
  readonly imageFrage: string | null;
  readonly imageLoesung: string | null;
  readonly urlFrage?: string;
  readonly urlLoesung?: string;
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
  imageFrage: null,
  imageLoesung: null
};



export const initialRaetselDetailsContent: RaetselDetailsContent = {
  kontext: "RAETSEL",
  raetsel: initialRaetselDetails,
  selectableDeskriptoren: []
};

