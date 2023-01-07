export type STATUS = 'ERFASST' | 'FREIGEGEBEN';
export type SortOrder = 'asc' | 'desc';

export const QUERY_PARAM_SUCHSTRING = 'suchstring';
export const QUERY_PARAM_DESKRIPTOREN = 'deskriptoren';
export const QUERY_PARAM_TYPE_DESKRIPTOREN = 'typeDeskriptoren';
export const QUERY_PARAM_LIMIT = 'limit';
export const QUERY_PARAM_OFFSET = 'offset';
export const QUERY_PARAM_SORT_DIRECTION = 'sortDirection';

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
  pageDefinition: PageDefinition
};

export const initialPageDefinition: PageDefinition = {
  pageSize: 20,
  pageIndex: 0,
  sortDirection: 'asc'
};

export const initialPaginationState: PaginationState = {
  anzahlTreffer: 0,
  pageDefinition: initialPageDefinition
};

export interface GeneratedImages {
  readonly imageFrage: string | null;
  readonly imageLoesung: string | null;
};

// #### PrintDialog
export type OUTPUTFORMAT = 'PDF' | 'PNG' | 'LATEX';
export type LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'ANKREUZTABELLE' | 'BUCHSTABEN' | 'DESCRIPTION' | 'NOOP';

export const anzeigeAntwortvorschlaegeSelectInput: string[] = [
    'ANKREUZTABELLE', 'BUCHSTABEN', 'DESCRIPTION', 'NOOP'
];

export interface GeneratedFile {
    readonly fileName: string,
    readonly fileData: Blob
};

export interface SelectPrintparametersDialogData {
    titel: string;
    layoutsAntwortvorschlaegeInput: string[];
    selectedLayoutAntwortvorschlaege?: string;
};


