export type Raetselursprung = 'EIGENBAU' | 'NACHBAU' | 'ZITAT';

export interface Antwortvorschlag {
  readonly buchstabe: string;
  readonly text?: string;
  readonly loesung: boolean;
};

/** vollständiges Set an Attributen für die Administration des Rätselkatalogs */
export interface RaetselDetails {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly text: string;
  readonly ursprung: Raetselursprung;
  readonly primaerquelleId: string;
  readonly sekundaerquelleId?: string;
  readonly antwortvorschlaege?: Antwortvorschlag[];
  readonly deskriptoren: string[];  
};

/** Minimalset an Attributen, die bei einer Suche geladen werden sollen */
export interface Raetsel {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly deskriptoren: string[];
};
