import { STATUS } from "@mja-workspace/shared/util-mja";

export type SortOrder = 'asc' | 'desc';


export type Schwierigkeitsgrad = 'AB_NEUN' |
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

export type Referenztyp = 'MINIKAENGURU' | 'SERIE';

export interface RaetselgruppeBasisdaten {
    readonly id: string;
    readonly name?: string;
    readonly kommentar?: string;
    readonly schwierigkeitsgrad?: Schwierigkeitsgrad;
    readonly referenztyp?: Referenztyp;
    readonly referenz?: string;
    readonly status: STATUS;
    readonly geaendertDurch?: string;
};

export const initialRaetselgruppeBasisdaten: RaetselgruppeBasisdaten = {
    id: 'neu',
    status: 'ERFASST'
};

export interface RaetselgruppensucheTrefferItem extends RaetselgruppeBasisdaten {
    readonly anzahlElemente: number;
};

export interface RaetselgruppensucheTreffer {
    readonly anzahlTreffer: number;
    readonly items: RaetselgruppensucheTrefferItem[];
};

export interface RaetselgruppenSuchparameter {
    name: string | null;
    schwierigkeitsgrad: Schwierigkeitsgrad | null;
    referenztyp: Referenztyp | null;
    referenz: string | null;
    sortAttribute: string;
    sortOrder: SortOrder;
    pageSize: number;
    pageIndex: number;
};

export const initialRaetselgruppenSuchparameter: RaetselgruppenSuchparameter = {
    name: null,
    schwierigkeitsgrad: null,
    referenztyp: null,
    referenz: null,
    pageIndex: 0,
    pageSize: 20,
    sortAttribute: 'name',
    sortOrder: 'asc'
};

export interface EditRaetselgruppePayload {
    readonly id: string;
    readonly name: string;
    readonly kommentar?: string;
    readonly schwierigkeitsgrad: Schwierigkeitsgrad;
    readonly referenztyp: Referenztyp;
    readonly referenz: string;
    readonly status: STATUS;
};

export interface EditRaetselgruppenelementPayload {
    readonly id: string;
    readonly nummer: string;
    readonly punkte: number;
    readonly raetselSchluessel: string;
}

export interface Raetselgruppenelement extends EditRaetselgruppenelementPayload {
    readonly name: string;
};

export interface RaetselgruppeDetails extends EditRaetselgruppePayload {
    readonly elemente: Raetselgruppenelement[];
};



