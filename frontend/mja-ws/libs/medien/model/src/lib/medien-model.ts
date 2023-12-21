export type Medienart = 'BUCH' | 'INTERNET' | 'ZEITSCHRIFT';

export interface MediumDto {
  readonly id: string;
  readonly medienart: Medienart | undefined;
  readonly titel: string | undefined;
  readonly autor: string | undefined;
  readonly url: string | undefined;
  readonly kommentar: string | undefined;
  readonly schreibgeschuetzt: boolean;

};

export const initialMediumDto: MediumDto = {
  id: 'neu',
  medienart: undefined,
  titel: undefined,
  autor: undefined,
  url: undefined,
  kommentar: undefined,
  schreibgeschuetzt: true
};


export interface MediensucheTrefferItem {
  readonly id: string;
  readonly medienart: Medienart;
  readonly titel: string;
  readonly kommentar: string | undefined;
};

export interface MediensucheResult {
  readonly trefferGesamt: number;
  readonly treffer: MediensucheTrefferItem[];
};


