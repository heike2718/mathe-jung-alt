import { Referenztyp, Schwierigkeitsgrad, SortOrder, STATUS } from '@mja-ws/core/model';

// export interface RaetselgruppeBasisdaten {
//   readonly id: string;
//   readonly name: string;
//   readonly schwierigkeitsgrad: Schwierigkeitsgrad;
//   readonly referenztyp: Referenztyp | undefined;
//   readonly referenz: string | undefined;
//   readonly status: STATUS;
// };

export interface RaetselgruppensucheTrefferItem {
  readonly id: string;
  readonly name: string;
  readonly schwierigkeitsgrad: Schwierigkeitsgrad;
  readonly referenztyp: Referenztyp | undefined;
  readonly referenz: string | undefined;
  readonly status: STATUS;
};

export interface RaetselgruppensucheTreffer {
  readonly trefferGesamt: number;
  readonly items: RaetselgruppensucheTrefferItem[];
};

export interface RaetselgruppenSuchparameter {
  sortAttribute: string;
  sortOrder: SortOrder;
  pageSize: number;
  pageIndex: number;
};

export const initialRaetselgruppenSuchparameter: RaetselgruppenSuchparameter = {
  sortAttribute: 'name',
  sortOrder: 'asc',
  pageSize: 20,
  pageIndex: 0
};
