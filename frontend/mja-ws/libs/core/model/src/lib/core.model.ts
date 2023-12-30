export type SortOrder = 'asc' | 'desc';
export type Referenztyp = 'NOOP' | 'MINIKAENGURU' | 'SERIE';
export type Quellenart = 'NOOP' | 'BUCH' | 'INTERNET' | 'PERSON' | 'ZEITSCHRIFT';
export type Medienart = 'NOOP' |  'BUCH' | 'INTERNET' | 'ZEITSCHRIFT';
export type Herkunftstyp = 'EIGENKREATION' | 'ZITAT' | 'ADAPTATION';

export const QUERY_PARAM_SUCHSTRING = 'suchstring';
export const QUERY_PARAM_DESKRIPTOREN = 'deskriptoren';
export const QUERY_PARAM_MODE_FULLTEXT_SEARCH = 'modeFullTextSearch';
export const QUERY_PARAM_SEARCH_MODE_FOR_DESCRIPTORS = 'searchModeForDescriptors';
export const QUERY_PARAM_TYPE_DESKRIPTOREN = 'typeDeskriptoren';
export const QUERY_PARAM_LIMIT = 'limit';
export const QUERY_PARAM_OFFSET = 'offset';
export const QUERY_PARAM_SORT_DIRECTION = 'sortDirection';
export const QUERY_PARAM_SORT_ATTRIBUTE = 'sortAttribute';

export type Schwierigkeitsgrad =
  'NOOP' |
  'ALLE' |
  'AB_NEUN' |
  'DREI_VIER' |
  'EINS' |
  'EINS_ZWEI' |
  'FUENF_SECHS' |
  'GRUNDSCHULE' |
  'IKID' |
  'SEK_1' |
  'SEK_2' |
  'SIEBEN_ACHT' |
  'VORSCHULE' |
  'ZWEI';

export interface MjaEntity {
  id: string | number
};

export interface SelectableItem extends MjaEntity {
  readonly id: string | number;
  readonly name: string;
  readonly selected: boolean;
};

export interface SelectItemsCompomentModel {
  ueberschriftAuswahlliste: string;
  ueberschriftGewaehlteItems: string;
  vorrat: SelectableItem[];
  gewaehlteItems: SelectableItem[];
};

export const initialSelectItemsComponentModel: SelectItemsCompomentModel = {
  ueberschriftAuswahlliste: '',
  ueberschriftGewaehlteItems: 'gewählt:',
  vorrat: [],
  gewaehlteItems: []
};

export interface UploadedFile {
  readonly name: string;
  readonly dataBase64: string
};

export const initialUploadedFile: UploadedFile = {
  name: '',
  dataBase64: ''
};

/** 
 * Neue Rätsel werden als EIGENKREATION mit der für den angemeldeten Admin oder Autor eingetragenen Quelle vom Typ PERSON angelegt.
 * Wenn eine andere Quelle zugewiesen wird, muss die Herkunft auf ZITAT oder ADAPTION geändert werden.
*/
export interface HerkunftRaetsel {
  readonly id: string;
  readonly quellenart: Quellenart;
  readonly herkunftstyp: Herkunftstyp;
  readonly text: string;
  readonly mediumUuid: string | undefined;
};

export const initialHerkunftRaetsel: HerkunftRaetsel = {
  id: 'neu',
  quellenart: 'PERSON',
  herkunftstyp: 'EIGENKREATION',
  mediumUuid: undefined,
  text: ''
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

// #### PrintDialoge
export type OutputFormat = 'PDF' | 'PNG' | 'LATEX';
export type LaTeXLayoutAntwortvorschlaege = 'ANKREUZTABELLE' | 'BUCHSTABEN' | 'DESCRIPTION' | 'NOOP';
export type FontName = 'DRUCK_BY_WOK' | 'FIBEL_NORD' | 'FIBEL_SUED' | 'STANDARD';
export type Verwendungszweck = 'ARBEITSBLATT' | 'KARTEI' | 'LATEX' | 'VORSCHAU';
export type Schriftgroesse = 'HUGE' | 'LARGE' | 'NORMAL';

export const anzeigeAntwortvorschlaegeSelectInput: string[] = [
  'Ankreuztabelle', 'Buchstaben', 'Liste'
];

export const fontNamenSelectInput: string[] = [
  'Druckschrift (Leseanfänger)',
  'Fibel Nord',
  'Fibel Süd',
  'Standardfont (LaTeX)'
];

export const verwendungszweckePublicSelectInput: string[] = [
  'Arbeitsblatt',
  'Kartei'
];

export const verwendungszweckeAutorenSelectInput: string[] = [
  'Arbeitsblatt',
  'Knobelkartei',
  'LaTeX',
  'Vorschau'
];

export const schriftgroessenSelectInput: string[] = [
  'normal',
  'groß',
  'sehr groß'
];

export interface GeneratedFile {
  readonly fileName: string,
  readonly fileData: Blob
};

// dies sind die Parameter für Dialoge zum Generieren für die admin-app. Es muss gewählt werden können zwischen verschiedenen Antwortvorschlag-Layouts und Fonts
export interface SelectGeneratorParametersUIModelAutoren {
  titel: string;
  showVerwendungszwecke: boolean;
  verwendungszwecke: string[];
  selectedVerwendungszweck: string | undefined;
  layoutsAntwortvorschlaegeInput: string[];
  selectedLayoutAntwortvorschlaege: string | undefined;
  fontNamen: string[];
  selectedFontName: string | undefined;
  schriftgroessen: string[];
  selectedSchriftgroesse: undefined;
};

// Für die public-API sollen die Menschen zwischen KARTEI und ARBEITSBLATT wählen dürfen.
export interface SelectGeneratorParametersUIModelPublic {
  verwendungszwecke: string[];
  selectedVerwendungszweck: string | undefined;
  fontNamen: string[];
  selectedFontName: string | undefined;
  schriftgroessen: string[];
  selectedSchriftgroesse: undefined;
}

// //////////////////////////////////

// Statistik
export interface AnzahlabfrageErgebnis {
  readonly ergebnis: number;
};
