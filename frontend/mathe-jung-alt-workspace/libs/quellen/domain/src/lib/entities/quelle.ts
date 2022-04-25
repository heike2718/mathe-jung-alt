export type Quellenart = 'BUCH' | 'PERSON' | 'ZEITSCHRIFT';

export interface Quelle {
  readonly uuid: string;
  readonly art: Quellenart;
  readonly mediumId?: string;
  readonly beschreibung: string;
};
