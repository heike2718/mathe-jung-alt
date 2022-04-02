export type Medienart = 'BUCH' | 'ZEITSCHRIFT';

export interface Medium {
  uuid: string;
  name: string;
  art: Medienart;
  uri?: string;
};
