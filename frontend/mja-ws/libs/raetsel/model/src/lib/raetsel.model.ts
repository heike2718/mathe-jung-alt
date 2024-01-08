import { DeskriptorUI, GeneratedImages, Herkunftstyp, Medienart, QuelleDto, Quellenart, Schwierigkeitsgrad, initialQuelleDto } from "@mja-ws/core/model";
import { EmbeddableImageInfo } from "@mja-ws/embeddable-images/model";

export type ModusVolltextsuche = 'UNION' | 'INTERSECTION';
export type ModusSucheMitDeskriptoren = 'LIKE' | 'NOT_LIKE';



export interface MediumQuelleDto {
  readonly id: string;
  readonly medienart: Medienart | undefined;
  readonly titel: string | undefined;
}

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
  readonly herkunft: Herkunftstyp;
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
  readonly antwortvorschlaegeEingebettet: boolean;
  readonly schreibgeschuetzt: boolean;
  readonly frage: string;
  readonly loesung: string | undefined;
  readonly kommentar: string | undefined;
  readonly herkunftstyp: Herkunftstyp;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: DeskriptorUI[];
  readonly images: GeneratedImages | null;
  readonly raetselPDF: Blob | null;
  readonly embeddableImageInfos: EmbeddableImageInfo[];
  readonly quelle: QuelleDto,
  readonly quellenangabe: string
};

export interface LinkedAufgabensammlung  {
  readonly id: string;
  readonly name: string;
  readonly nummer: string;
  readonly punkte: number;
  readonly schwierigkeitsgrad: Schwierigkeitsgrad;
  readonly freigegeben: boolean;
  readonly privat: boolean;
  readonly owner: string;
};

/** 
 * Payload zum Anlegen oder Ändern eines Rätsels
*/
export const initialRaetselDetails: RaetselDetails = {
  id: 'neu',
  schluessel: '',
  name: '',
  freigegeben: false,
  antwortvorschlaegeEingebettet: false,
  schreibgeschuetzt: true,
  frage: '',
  loesung: '',
  kommentar: '',
  herkunftstyp: 'EIGENKREATION',
  antwortvorschlaege: [],
  deskriptoren: [],
  images: null,
  raetselPDF: null,
  embeddableImageInfos: [],
  quelle: initialQuelleDto,
  quellenangabe: ''
};

export interface EditRaetselPayload {
  readonly id: string;
  readonly schluessel: string | null;
  readonly latexHistorisieren: boolean;
  readonly name: string;
  readonly freigegeben: boolean;
  readonly antwortvorschlaegeEingebettet: boolean;
  readonly herkunftstyp: Herkunftstyp;
  readonly frage: string;
  readonly loesung: string | undefined;
  readonly kommentar: string | undefined;
  readonly antwortvorschlaege: Antwortvorschlag[];
  readonly deskriptoren: DeskriptorUI[];
  readonly quelle: QuelleDto;
};

export interface GUIEditRaetselPayload {
  readonly editRaetselPayload: EditRaetselPayload;
  readonly quellenangabe: string;
  readonly embeddableImageInfos: EmbeddableImageInfo[];
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

export function createEditRaetselPayload(raetselDetails: RaetselDetails): GUIEditRaetselPayload {
  const editRaetselPayload: EditRaetselPayload = {
    latexHistorisieren: false,
    antwortvorschlaege: raetselDetails.antwortvorschlaege,
    deskriptoren: raetselDetails.deskriptoren,
    frage: raetselDetails.frage,
    freigegeben: raetselDetails.freigegeben,
    antwortvorschlaegeEingebettet: raetselDetails.antwortvorschlaegeEingebettet,
    herkunftstyp: raetselDetails.herkunftstyp,
    id: raetselDetails.id,
    kommentar: raetselDetails.kommentar,
    loesung: raetselDetails.loesung,
    name: raetselDetails.name,
    schluessel: raetselDetails.schluessel.length > 0 ? raetselDetails.schluessel : null,
    quelle: raetselDetails.quelle
  };

  const quellenangabe = raetselDetails.quellenangabe;

  return {editRaetselPayload: editRaetselPayload, quellenangabe: quellenangabe, embeddableImageInfos: raetselDetails.embeddableImageInfos};
}

// ////////////////////////////////////////////////////////////////////////////////////
//    helper classes for the mapping between Java enums and UI-Models
// ////////////////////////////////////////////////////////////////////////////////////

export interface GuiQuellenart {
  readonly id: Quellenart;
  readonly label: string;
};

export const initialGuiQuellenart: GuiQuellenart = {
  id: 'PERSON',
  label: ''
};
export class GuiQuellenartenMap {

  #quellenarten: Map<Quellenart, string> = new Map();
  #quellenartenInvers: Map<string, Quellenart> = new Map();

  constructor() {

    this.#quellenarten.set('BUCH', 'Buch');
    this.#quellenarten.set('INTERNET', 'Internet');
    this.#quellenarten.set('PERSON', 'Person');
    this.#quellenarten.set('ZEITSCHRIFT', 'Zeitschrift');

    this.#quellenartenInvers.set('Buch', 'BUCH');
    this.#quellenartenInvers.set('Internet', 'INTERNET');
    this.#quellenartenInvers.set('Person', 'PERSON');
    this.#quellenartenInvers.set('Zeitschrift', 'ZEITSCHRIFT');
  }

  public getQuellenartOfLabel(label: string): Quellenart {

    const value: Quellenart | undefined = this.#quellenartenInvers.get(label);
    return value ? value : 'PERSON';
  }

  public getLabelOfQuellenart(quellenart: Quellenart): string {
    const value = this.#quellenarten.get(quellenart);
    return value ? value : '';
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

export const initialGuiHerkunftstyp: GuiHerkunfsttyp = {
  id: 'EIGENKREATION',
  label: ''
};
export class GuiHerkunftstypenMap {

  #herkunftstypen: Map<Herkunftstyp, string> = new Map();
  #herkunftstypenInvers: Map<string, Herkunftstyp> = new Map();

  constructor() {

    this.#herkunftstypen.set('EIGENKREATION', 'Eigenkreation');
    this.#herkunftstypen.set('ADAPTION', 'Adaption');
    this.#herkunftstypen.set('ZITAT', 'Zitat');

    this.#herkunftstypenInvers.set('Eigenkreation', 'EIGENKREATION');
    this.#herkunftstypenInvers.set('Adaption', 'ADAPTION');
    this.#herkunftstypenInvers.set('Zitat', 'ZITAT');
  }

  public getHerkunftstypOfLabel(label: string): Herkunftstyp {

    const value: Herkunftstyp | undefined = this.#herkunftstypenInvers.get(label);
    return value ? value : 'EIGENKREATION';
  }

  public getGuiHerkunftstyp(refTyp: Herkunftstyp): GuiHerkunfsttyp {

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
