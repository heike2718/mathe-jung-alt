import { Referenztyp, Schwierigkeitsgrad, SortOrder, STATUS } from '@mja-ws/core/model';

// export interface RaetselgruppeBasisdaten {
//   readonly id: string;
//   readonly name: string;
//   readonly schwierigkeitsgrad: Schwierigkeitsgrad;
//   readonly referenztyp: Referenztyp | undefined;
//   readonly referenz: string | undefined;
//   readonly status: STATUS;
// };

export interface RaetselgruppenTrefferItem {
  readonly id: string;
  readonly name: string;
  readonly schwierigkeitsgrad: Schwierigkeitsgrad;
  readonly referenztyp: Referenztyp | undefined;
  readonly referenz: string | undefined;
  readonly status: STATUS;
};

export interface RaetselgruppenTreffer {
  readonly trefferGesamt: number;
  readonly items: RaetselgruppenTrefferItem[];
};

export interface RaetselgruppenSuchparameter {
  name: string | null;
  schwierigkeitsgrad: Schwierigkeitsgrad | null;
  referenztyp: Referenztyp | null;
  referenz: string | null;
  sortAttribute: string;
};

export const initialRaetselgruppenSuchparameter: RaetselgruppenSuchparameter = {
  name: null,
  schwierigkeitsgrad: null,
  referenztyp: null,
  referenz: null,
  sortAttribute: 'name',  
};

export interface EditRaetselgruppePayload {
  readonly id: string;
    readonly name: string;
    readonly kommentar: string | undefined;
    readonly schwierigkeitsgrad: Schwierigkeitsgrad;
    readonly referenztyp: Referenztyp | undefined;
    readonly referenz: string | undefined;
    readonly status: STATUS;
};

export interface EditRaetselgruppenelementPayload {
  readonly id: string;
  readonly nummer: string;
  readonly punkte: number;
  readonly raetselSchluessel: string;
};

export interface Raetselgruppenelement extends EditRaetselgruppenelementPayload {
  readonly name: string;
  readonly loesungsbuchstabe?: string;
};

export interface RaetselgruppeDetails extends EditRaetselgruppePayload {
  readonly elemente: Raetselgruppenelement[];
  readonly geaendertDurch: string;
  readonly schreibgeschuetzt: boolean;
};

export function isInitialRaetselgruppenSuchparameter(raetselgruppenSuchparameter: RaetselgruppenSuchparameter): boolean {


  if (raetselgruppenSuchparameter.name !== initialRaetselgruppenSuchparameter.name) {
    return false;
  }
  if (raetselgruppenSuchparameter.referenz !== initialRaetselgruppenSuchparameter.referenz) {
    return false;
  }
  if (raetselgruppenSuchparameter.referenztyp !== initialRaetselgruppenSuchparameter.referenztyp) {
    return false;
  }
  if (raetselgruppenSuchparameter.schwierigkeitsgrad !== initialRaetselgruppenSuchparameter.schwierigkeitsgrad) {
    return false;
  }

  return true;
}
