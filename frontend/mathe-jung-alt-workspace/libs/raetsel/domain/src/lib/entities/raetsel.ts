import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";

export interface Antwortvorschlag {
  readonly buchstabe: string;
  readonly text?: string;
  readonly korrekt: boolean;
};

/** vollständiges Set an Attributen für die Administration des Rätselkatalogs */
/*
{
  "id": "7a94e100-85e9-4ffb-903b-06835851063b",
  "schluessel": "02789",
  "name": "2022 zählen",
  "frage": "\\begin{center}{\\Large \\bf 2 0 2 2 2 0 2 2 2 0 2 2 2 0 2 2 2 0 2 2 0 0 2 2 2 0 2 2 2 0 2 2 2 0 2 2}\\end{center}Wie oft steht hier 2022?",
  "loesung": "alle 2022 umranden, dann zählen",
  "kommentar": "Minikänguru 2022 Klasse 1",
  "quelleId": "8ef4d9b8-62a6-4643-8674-73ebaec52d98",
  "antwortvorschlaege": [],
  "deskriptoren": []
}
*/

export interface RaetselDetails {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly frage: string;
  readonly loesung?: string;
  readonly kommentar?: string;
  readonly quelleId: string;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: Deskriptor[];
  readonly imageFrage: string | null;
  readonly imageLoesung: string | null;  
};

/** Minimalset an Attributen, die bei einer Suche geladen werden sollen */
export interface Raetsel {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly deskriptoren: Deskriptor[];
};

export interface EditRaetselPayload {
  readonly latexHistorisieren: boolean
  readonly raetsel: RaetselDetails
};

export const initialRaetselDetails: RaetselDetails = {
  id: 'neu',
  schluessel: '',
  name: '',
  frage: '',
  loesung: '',
  kommentar: '',
  quelleId: '',
  antwortvorschlaege: [],
  deskriptoren: [],
  imageFrage: null,
  imageLoesung: null
};
