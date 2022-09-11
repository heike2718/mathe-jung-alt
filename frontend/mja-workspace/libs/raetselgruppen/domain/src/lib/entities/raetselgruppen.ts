import { STATUS } from "@mja-workspace/shared/util-mja";

export type SortOrder = 'asc' | 'desc' |'noop';


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
    name?: string;
    // sortName: SortOrder;
    schwierigkeitsgrad?: Schwierigkeitsgrad;
    // sortSchwierigkeitsgrad: SortOrder;
    referenztyp?: Referenztyp;
    // sortReferenztyp: SortOrder;
    referenz?: string;
    // sortReferenz: SortOrder;
    pageSize: number;
    pageIndex: number;
};

export const initialRaetselgruppenSuchparameter: RaetselgruppenSuchparameter = {
    pageIndex: 0,
    pageSize: 20,
    // sortName: 'asc',
    // sortSchwierigkeitsgrad: 'noop',
    // sortReferenztyp: 'noop' ,
    // sortReferenz: 'noop'   
}



