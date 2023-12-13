//export type STATUS = 'ERFASST' | 'FREIGEGEBEN';
export type SortOrder = 'asc' | 'desc';
export type Referenztyp = 'NOOP' | 'MINIKAENGURU' | 'SERIE';
export type Quellenart = 'BUCH' | 'INTERNET' | 'PERSON' | 'ZEITSCHRIFT';
export type Medienart = 'BUCH' | 'INTERNET' | 'ZEITSCHRIFT';
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
export interface GuiRefereztyp {
  readonly id: Referenztyp;
  readonly label: string;
};

export const initialGuiReferenztyp: GuiRefereztyp = { id: 'NOOP', label: '' };

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
export type OUTPUTFORMAT = 'PDF' | 'PNG' | 'LATEX';
export type LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'ANKREUZTABELLE' | 'BUCHSTABEN' | 'DESCRIPTION' | 'NOOP';
export type FONT_NAME = 'DRUCK_BY_WOK' | 'FIBEL_NORD' | 'FIBEL_SUED' | 'STANDARD';
export type VERWENDUNGSZWECK = 'ARBEITSBLATT' | 'KARTEI' | 'LATEX' | 'VORSCHAU';
export type SCHRIFTGROESSE = 'HUGE' | 'LARGE' | 'NORMAL';

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

export class GuiReferenztypenMap {

  #referenztypen: Map<Referenztyp, string> = new Map();
  #referenztypenInvers: Map<string, Referenztyp> = new Map();

  constructor() {
    this.#referenztypen.set('NOOP', '');
    this.#referenztypen.set('MINIKAENGURU', 'Minikänguru');
    this.#referenztypen.set('SERIE', 'Serie');

    this.#referenztypenInvers.set('', 'NOOP');
    this.#referenztypenInvers.set('Minikänguru', 'MINIKAENGURU');
    this.#referenztypenInvers.set('Serie', 'SERIE');
  }

  public getReferenztypOfLabel(label: string): Referenztyp {

    const value = this.#referenztypenInvers.get(label);
    return value ? value : 'NOOP';
  }

  public getGuiRefereztyp(refTyp: Referenztyp): GuiRefereztyp {

    if (this.#referenztypen.has(refTyp)) {
      const label = this.#referenztypen.get(refTyp);

      if (label) {
        return { id: refTyp, label: label };
      } else {
        return initialGuiReferenztyp;
      }

    }

    return initialGuiReferenztyp;
  }

  public toGuiArray(): GuiRefereztyp[] {

    const result: GuiRefereztyp[] = [];
    this.#referenztypen.forEach((l: string, key: Referenztyp) => {
      result.push({ id: key, label: l });
    });

    return result;
  }

  public getLabelsSorted(): string[] {

    const result: string[] = [];

    this.toGuiArray().forEach(element => result.push(element.label));
    return result;
  }


};

export interface GuiSchwierigkeitsgrad {
  readonly id: Schwierigkeitsgrad;
  readonly label: string;
};

export const initialGuiSchwierigkeitsgrad: GuiSchwierigkeitsgrad = { id: 'NOOP', label: '' };

export class GuiSchwierigkeitsgradeMap {

  #schwierigkeitsgrade: Map<Schwierigkeitsgrad, string> = new Map();
  #schwierigkeitsgradeInvers: Map<string, Schwierigkeitsgrad> = new Map();

  constructor() {
    this.#schwierigkeitsgrade.set('NOOP', '');
    this.#schwierigkeitsgrade.set('IKID', 'Inklusion');
    this.#schwierigkeitsgrade.set('EINS', 'Klasse 1');
    this.#schwierigkeitsgrade.set('ZWEI', 'Klasse 2');
    this.#schwierigkeitsgrade.set('EINS_ZWEI', 'Klassen 1/2');
    this.#schwierigkeitsgrade.set('DREI_VIER', 'Klassen 3/4');
    this.#schwierigkeitsgrade.set('FUENF_SECHS', 'Klassen 5/6');
    this.#schwierigkeitsgrade.set('SIEBEN_ACHT', 'Klassen 7/8');
    this.#schwierigkeitsgrade.set('AB_NEUN', 'ab Klasse 9');
    this.#schwierigkeitsgrade.set('VORSCHULE', 'Vorschule');
    this.#schwierigkeitsgrade.set('GRUNDSCHULE', 'Grundschule');
    this.#schwierigkeitsgrade.set('SEK_1', 'Sekundarstufe 1');
    this.#schwierigkeitsgrade.set('SEK_2', 'Sekundarstufe 2');
    this.#schwierigkeitsgrade.set('ALLE', 'von Vorschule bis Erwachsene');

    this.#schwierigkeitsgradeInvers.set('', 'NOOP');
    this.#schwierigkeitsgradeInvers.set('Inklusion', 'IKID');
    this.#schwierigkeitsgradeInvers.set('Klasse 1', 'EINS');
    this.#schwierigkeitsgradeInvers.set('Klasse 2', 'ZWEI');
    this.#schwierigkeitsgradeInvers.set('Klassen 1/2', 'EINS_ZWEI');
    this.#schwierigkeitsgradeInvers.set('Klassen 3/4', 'DREI_VIER');
    this.#schwierigkeitsgradeInvers.set('Klassen 5/6', 'FUENF_SECHS');
    this.#schwierigkeitsgradeInvers.set('Klassen 7/8', 'SIEBEN_ACHT');
    this.#schwierigkeitsgradeInvers.set('ab Klasse 9', 'AB_NEUN');
    this.#schwierigkeitsgradeInvers.set('Vorschule', 'VORSCHULE');
    this.#schwierigkeitsgradeInvers.set('Grundschule', 'GRUNDSCHULE');
    this.#schwierigkeitsgradeInvers.set('Sekundarstufe 1', 'SEK_1');
    this.#schwierigkeitsgradeInvers.set('Sekundarstufe 2', 'SEK_2');
    this.#schwierigkeitsgradeInvers.set('von Vorschule bis Erwachsene', 'ALLE');
  }

  public getSchwierigkeitsgradOfLabel(label: string): Schwierigkeitsgrad {

    const value: Schwierigkeitsgrad | undefined = this.#schwierigkeitsgradeInvers.get(label);
    return value ? value : 'NOOP';
  }

  public getGuiSchwierigkeitsgrade(refTyp: Schwierigkeitsgrad): GuiSchwierigkeitsgrad {

    if (this.#schwierigkeitsgrade.has(refTyp)) {

      const label = this.#schwierigkeitsgrade.get(refTyp);

      if (label) {
        return { id: refTyp, label: label };
      } else {
        return initialGuiSchwierigkeitsgrad;
      }
    }

    return initialGuiSchwierigkeitsgrad;
  }

  public toGuiArray(): GuiSchwierigkeitsgrad[] {

    const result: GuiSchwierigkeitsgrad[] = [];
    this.#schwierigkeitsgrade.forEach((l: string, key: Schwierigkeitsgrad) => {
      result.push({ id: key, label: l });
    });

    return result;
  }

  public getLabelsSorted(): string[] {

    const result: string[] = [];

    this.toGuiArray().forEach(element => result.push(element.label));
    return result;
  }
}

// Statistik
export interface AnzahlabfrageErgebnis {
  readonly ergebnis: number;
};
