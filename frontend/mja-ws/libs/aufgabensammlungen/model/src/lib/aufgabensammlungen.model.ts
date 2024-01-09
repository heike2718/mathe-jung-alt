import { Herkunftstyp, Referenztyp, Schwierigkeitsgrad } from '@mja-ws/core/model';


export interface GuiRefereztyp {
  readonly id: Referenztyp;
  readonly label: string;
};

export const initialGuiReferenztyp: GuiRefereztyp = { id: 'NOOP', label: '' };

export interface AufgabensammlungBasisdaten {
  readonly id: string;
  readonly name: string;
  readonly kommentar: string | undefined;
  readonly schwierigkeitsgrad: Schwierigkeitsgrad;
  readonly referenztyp: Referenztyp | undefined;
  readonly referenz: string | undefined;
  readonly freigegeben: boolean;
  readonly privat: boolean;
  readonly geaendertDurch: string | undefined;
};

export interface AufgabensammlungTrefferItem {
  readonly id: string;
  readonly name: string;
  readonly schwierigkeitsgrad: Schwierigkeitsgrad;
  readonly referenztyp: Referenztyp | undefined;
  readonly referenz: string | undefined;
  readonly freigegeben: boolean;
  readonly privat: boolean;
  readonly geaendertDurch: string | undefined;
  readonly anzahlElemente: number;
};

export interface AufgabensammlungenTreffer {
  readonly trefferGesamt: number;
  readonly items: AufgabensammlungTrefferItem[];
};

export interface AufgabensammlungenSuchparameter {
  name: string | null;
  schwierigkeitsgrad: Schwierigkeitsgrad | null;
  referenztyp: Referenztyp | null;
  referenz: string | null;
  sortAttribute: string;
};

export const initialAufgabensammlungenSuchparameter: AufgabensammlungenSuchparameter = {
  name: null,
  schwierigkeitsgrad: null,
  referenztyp: null,
  referenz: null,
  sortAttribute: 'name',
};

export interface EditAufgabensammlungPayload {
  readonly id: string;
  readonly name: string;
  readonly kommentar: string | undefined;
  readonly schwierigkeitsgrad: Schwierigkeitsgrad;
  readonly referenztyp: Referenztyp | undefined;
  readonly referenz: string | undefined;
  readonly freigegeben: boolean;
  readonly privat: boolean;
};

export interface EditAufgabensammlungselementPayload {
  readonly id: string;
  readonly nummer: string;
  readonly punkte: number;
  readonly raetselSchluessel: string;
};

export interface Aufgabensammlungselement extends EditAufgabensammlungselementPayload {
  readonly name: string;
  readonly herkunftstyp: Herkunftstyp;
  readonly loesungsbuchstabe: string | undefined;
};

export interface AufgabensammlungDetails extends EditAufgabensammlungPayload {
  readonly elemente: Aufgabensammlungselement[];
  readonly geaendertDurch: string;
  readonly schreibgeschuetzt: boolean;
};

export const initialAufgabensammlungDetails: AufgabensammlungDetails = {
  id: 'neu',
  elemente: [],
  freigegeben: false,
  geaendertDurch: '',
  kommentar: undefined,
  name: '',
  privat: true,
  referenz: undefined,
  referenztyp: 'NOOP',
  schreibgeschuetzt: false,
  schwierigkeitsgrad: 'NOOP'
};

export function isInitialAufgabensammlungenSuchparameter(aufgabensammlungenSuchparameter: AufgabensammlungenSuchparameter): boolean {


  if (aufgabensammlungenSuchparameter.name !== initialAufgabensammlungenSuchparameter.name) {
    return false;
  }
  if (aufgabensammlungenSuchparameter.referenz !== initialAufgabensammlungenSuchparameter.referenz) {
    return false;
  }
  if (aufgabensammlungenSuchparameter.referenztyp !== initialAufgabensammlungenSuchparameter.referenztyp) {
    return false;
  }
  if (aufgabensammlungenSuchparameter.schwierigkeitsgrad !== initialAufgabensammlungenSuchparameter.schwierigkeitsgrad) {
    return false;
  }

  return true;
}

// ////////////////////////////////////////////////////////////////////////////////////
//    helper classes for the mapping between Java enums and UI-Models
// ////////////////////////////////////////////////////////////////////////////////////

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
