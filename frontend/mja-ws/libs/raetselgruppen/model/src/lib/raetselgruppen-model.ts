import { Referenztyp, Schwierigkeitsgrad, SortOrder, STATUS } from '@mja-ws/core/model';

// export interface RaetselgruppeBasisdaten {
//   readonly id: string;
//   readonly name: string;
//   readonly schwierigkeitsgrad: Schwierigkeitsgrad;
//   readonly referenztyp: Referenztyp | undefined;
//   readonly referenz: string | undefined;
//   readonly status: STATUS;
// };

export interface RaetselgruppenTrefferItem {
  readonly id: string;
  readonly name: string;
  readonly schwierigkeitsgrad: Schwierigkeitsgrad;
  readonly referenztyp: Referenztyp | undefined;
  readonly referenz: string | undefined;
  readonly status: STATUS;
};

export interface RaetselgruppenTreffer {
  readonly trefferGesamt: number;
  readonly items: RaetselgruppenTrefferItem[];
};

export interface RaetselgruppenSuchparameter {
  name: string | null;
  schwierigkeitsgrad: Schwierigkeitsgrad | null;
  referenztyp: Referenztyp | null;
  referenz: string | null;
  sortAttribute: string;
};

export const initialRaetselgruppenSuchparameter: RaetselgruppenSuchparameter = {
  name: null,
  schwierigkeitsgrad: null,
  referenztyp: null,
  referenz: null,
  sortAttribute: 'name',  
};
