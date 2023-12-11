import { Referenztyp, Schwierigkeitsgrad } from '@mja-ws/core/model';

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

export const initialAufgabensammlungBasisdaten: AufgabensammlungBasisdaten = {
  id: 'neu',
  name: '',
  freigegeben: false,
  privat: false,
  kommentar: undefined,
  schwierigkeitsgrad: 'NOOP',
  referenztyp: 'NOOP',
  referenz: undefined,
  geaendertDurch: undefined
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
  readonly loesungsbuchstabe: string | undefined;
};

export interface AufgabensammlungDetails extends EditAufgabensammlungPayload {
  readonly elemente: Aufgabensammlungselement[];
  readonly geaendertDurch: string;
  readonly schreibgeschuetzt: boolean;
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
