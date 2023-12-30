import { DeskriptorUI, GeneratedImages, HerkunftRaetsel, Herkunftstyp, Quellenart, initialHerkunftRaetsel } from "@mja-ws/core/model";
import { EmbeddableImageInfo } from "@mja-ws/embeddable-images/model";

export type ModusVolltextsuche = 'UNION' | 'INTERSECTION';
export type ModusSucheMitDeskriptoren = 'LIKE' | 'NOT_LIKE';

export interface QuelleDto {
  readonly id: string;
  readonly quellenart: Quellenart;
  readonly klasse: string | undefined;
  readonly stufe: string | undefined;
  readonly ausgabe: string | undefined;
  readonly jahr: string | undefined;
  readonly seite: string| undefined;
  readonly person: string | undefined;
};

export const initialQuelleDto: QuelleDto = {
  id: 'neu',
  quellenart: 'PERSON',
  ausgabe: undefined,
  jahr: undefined,
  klasse: undefined,
  person: undefined,
  seite: undefined,
  stufe: undefined
};

export interface RaetselSuchfilter {
  readonly suchstring: string;
  readonly deskriptoren: DeskriptorUI[];
  readonly modeFullTextSearch: ModusVolltextsuche;
  readonly searchModeForDescriptors: ModusSucheMitDeskriptoren;
};

export const initialRaetselSuchfilter: RaetselSuchfilter = {
  suchstring: '',
  deskriptoren: [],
  modeFullTextSearch: 'UNION',
  searchModeForDescriptors: 'LIKE'
};

export interface Antwortvorschlag {
  readonly buchstabe: string;
  readonly text: string | undefined;
  readonly korrekt: boolean;
};

export interface RaetselsucheTreffer {
  readonly trefferGesamt: number;
  readonly treffer: Raetsel[];
};

/** Minimalset an Attributen, die bei einer Suche geladen werden sollen */
export interface Raetsel {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly freigegeben: boolean;
  readonly kommentar: string | undefined;
  readonly deskriptoren: DeskriptorUI[];
};

/**
 * Details eines Rätsels zum Anzeigen in der Detailansicht
 */
export interface RaetselDetails {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly freigegeben: boolean;
  readonly schreibgeschuetzt: boolean;
  readonly frage: string;
  readonly loesung: string | undefined;
  readonly kommentar: string | undefined;
  readonly herkunft: HerkunftRaetsel;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: DeskriptorUI[];
  readonly images: GeneratedImages | null;
  readonly raetselPDF: Blob | null;
  readonly embeddableImageInfos: EmbeddableImageInfo[];
};

/** 
 * Payload zum Anlegen oder Ändern eines Rätsels
*/
export const initialRaetselDetails: RaetselDetails = {
  id: 'neu',
  schluessel: '',
  name: '',
  freigegeben: false,
  schreibgeschuetzt: true,
  frage: '',
  loesung: '',
  kommentar: '',
  herkunft: initialHerkunftRaetsel,
  antwortvorschlaege: [],
  deskriptoren: [],
  images: null,
  raetselPDF: null,
  embeddableImageInfos: []
};

export interface EditRaetselPayload {
  readonly id: string;
  readonly schluessel: string | null;
  readonly latexHistorisieren: boolean;
  readonly name: string;
  readonly freigegeben: boolean;
  readonly herkunftstyp: Herkunftstyp;
  readonly frage: string;
  readonly loesung: string | undefined;
  readonly kommentar: string | undefined;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: DeskriptorUI[];
  readonly quelle: QuelleDto;
};

export function deskriptorenToString(deskriptoren: DeskriptorUI[]): string {

  if (deskriptoren.length === 0) {
    return '';
  }

  let result = '';

  for (let index = 0; index < deskriptoren.length; index++) {

    const deskriptor = deskriptoren[index];
    if (index < deskriptoren.length - 1) {
      result += deskriptor.name + ", ";
    } else {
      result += deskriptor.name;
    }

  }

  return result;
};

export function isSuchfilterEmpty(suchfilter: RaetselSuchfilter): boolean {
  return suchfilter.suchstring === '' && suchfilter.deskriptoren.length === 0;
};

// ////////////////////////////////////////////////////////////////////////////////////
//    helper classes for the mapping between Java enums and UI-Models
// ////////////////////////////////////////////////////////////////////////////////////

export interface GuiQuellenart {
  readonly id: Quellenart;
  readonly label: string;
};

export const initialGuiQuellenart : GuiQuellenart = {
  id: 'NOOP',
  label: ''
};
export class GuiQuellenartenMap {

  #quellenarten: Map<Quellenart, string> = new Map();
  #quellenartenInvers: Map<string, Quellenart> = new Map();

  constructor() {

    this.#quellenarten.set('NOOP', '');
    this.#quellenarten.set('BUCH', 'Buch');
    this.#quellenarten.set('INTERNET', 'Internet');
    this.#quellenarten.set('PERSON', 'Person');
    this.#quellenarten.set('ZEITSCHRIFT', 'Zeitschrift');

    this.#quellenartenInvers.set('', 'NOOP');
    this.#quellenartenInvers.set('Buch', 'BUCH');
    this.#quellenartenInvers.set('Internet', 'INTERNET');
    this.#quellenartenInvers.set('Person', 'PERSON');
    this.#quellenartenInvers.set('Zeitschrift', 'ZEITSCHRIFT');
  }

  public getQuellenartOfLabel(label: string): Quellenart {

    const value: Quellenart | undefined = this.#quellenartenInvers.get(label);
    return value ? value : 'NOOP';
  }

  public getGuiQuellenart(refTyp: Quellenart): GuiQuellenart {

    if (this.#quellenarten.has(refTyp)) {

      const label = this.#quellenarten.get(refTyp);

      if (label) {
        return { id: refTyp, label: label };
      } else {
        return initialGuiQuellenart;
      }
    }

    return initialGuiQuellenart;
  }

  public toGuiArray(): GuiQuellenart[] {

    const result: GuiQuellenart[] = [];
    this.#quellenarten.forEach((l: string, key: Quellenart) => {
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

// ////////////////////////////

export interface GuiHerkunfsttyp {
  readonly id: Herkunftstyp;
  readonly label: string;
};

export const initialGuiHerkunftstyp : GuiHerkunfsttyp = {
  id: 'EIGENKREATION',
  label: ''
};
export class GuiHerkunftstypenMap {

  #herkunftstypen: Map<Herkunftstyp, string> = new Map();
  #herkunftstypenInvers: Map<string, Herkunftstyp> = new Map();

  constructor() {

    this.#herkunftstypen.set('EIGENKREATION', 'Eigenkreation');
    this.#herkunftstypen.set('ADAPTATION', 'Adaption');
    this.#herkunftstypen.set('ZITAT', 'Zitat');

    this.#herkunftstypenInvers.set('Eigenkreation', 'EIGENKREATION');
    this.#herkunftstypenInvers.set('Adaption', 'ADAPTATION');
    this.#herkunftstypenInvers.set('Zitat', 'ZITAT');
  }

  public getHerkunftstypOfLabel(label: string): Herkunftstyp {

    const value: Herkunftstyp | undefined = this.#herkunftstypenInvers.get(label);
    return value ? value : 'EIGENKREATION';
  }

  public getGuiQuellenart(refTyp: Herkunftstyp): GuiHerkunfsttyp {

    if (this.#herkunftstypen.has(refTyp)) {

      const label = this.#herkunftstypen.get(refTyp);

      if (label) {
        return { id: refTyp, label: label };
      } else {
        return initialGuiHerkunftstyp;
      }
    }

    return initialGuiHerkunftstyp;
  }

  public toGuiArray(): GuiHerkunfsttyp[] {

    const result: GuiHerkunfsttyp[] = [];
    this.#herkunftstypen.forEach((l: string, key: Herkunftstyp) => {
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
