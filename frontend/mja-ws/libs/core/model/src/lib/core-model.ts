export type STATUS = 'ERFASST' | 'FREIGEGEBEN';
export type SortOrder = 'asc' | 'desc';

/** 
 * Ein angemeldeter ADMIN bzw. AUTOR ist selbst eine Quelle. Dies ist die zugeordnete Quelle.
 * Alle Raetsel und davon abgeleiteten Objekte referenzieren eine Quelle. Dies ist die zugeordnete Quelle 
*/
export interface QuelleUI {
  readonly id: string;
  readonly name: string;
};

export const noopQuelle: QuelleUI = {
  id: 'NOOP',
  name: ''
};

export interface DeskriptorUI {
  readonly id: number;
  readonly name: string;
};

export interface PageDefinition {
  pageSize: number,
  pageIndex: number,
  sortDirection: string
};

export interface PaginationState {
  anzahlTreffer: number;
  pageSize: number,
  pageIndex: number,
  sortDirection: string
};

export const initialPaginationState: PaginationState = {
  anzahlTreffer: 0,
  pageSize: 20,
  pageIndex: 0,
  sortDirection: 'asc'
};

export interface GeneratedImages {
  readonly imageFrage: string | null;
  readonly imageLoesung: string | null;
};
