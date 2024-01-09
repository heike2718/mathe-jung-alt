export type SortOrder = 'asc' | 'desc';
export type Referenztyp = 'NOOP' | 'MINIKAENGURU' | 'SERIE';
export type Quellenart = 'BUCH' | 'INTERNET' | 'PERSON' | 'ZEITSCHRIFT';
export type Medienart = 'NOOP' | 'BUCH' | 'INTERNET' | 'ZEITSCHRIFT';
export type Herkunftstyp = 'EIGENKREATION' | 'ZITAT' | 'ADAPTION';

export const QUERY_PARAM_SUCHSTRING = 'suchstring';
export const QUERY_PARAM_DESKRIPTOREN = 'deskriptoren';
export const QUERY_PARAM_MODE_FULLTEXT_SEARCH = 'modeFullTextSearch';
export const QUERY_PARAM_SEARCH_MODE_FOR_DESCRIPTORS = 'searchModeForDescriptors';
export const QUERY_PARAM_TYPE_DESKRIPTOREN = 'typeDeskriptoren';
export const QUERY_PARAM_LIMIT = 'limit';
export const QUERY_PARAM_OFFSET = 'offset';
export const QUERY_PARAM_SORT_DIRECTION = 'sortDirection';
export const QUERY_PARAM_SORT_ATTRIBUTE = 'sortAttribute';

export interface QuelleDto {
  readonly id: string;
  quellenart: Quellenart;
  klasse: string | undefined;
  stufe: string | undefined;
  ausgabe: string | undefined;
  jahr: string | undefined;
  seite: string | undefined;
  pfad: string | undefined;
  person: string | undefined;
  mediumUuid: string | undefined;
};

export const initialQuelleDto: QuelleDto = {
  id: 'neu',
  quellenart: 'PERSON',
  ausgabe: undefined,
  jahr: undefined,
  klasse: undefined,
  person: undefined,
  seite: undefined,
  stufe: undefined,
  pfad: undefined,
  mediumUuid: undefined
};

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

export interface SelectableItem {
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
  'Kartei',
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

// dies sind die Parameter für Dialoge zum Generieren von Aufgabensammlungen oder Rätseln
// Es muss gewählt werden können zwischen verschiedenen Antwortvorschlag-Layouts und Fonts
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

// //////////////////////////////////

// Statistik
export interface AnzahlabfrageErgebnis {
  readonly ergebnis: number;
};
