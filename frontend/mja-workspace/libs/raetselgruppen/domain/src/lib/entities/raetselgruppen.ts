import { STATUS } from "@mja-workspace/shared/util-mja";


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

export interface RaetselgruppensucheTrefferItem {
    readonly id: string;
    readonly name: string;
    readonly kommentar?: string;
    readonly schwierigkeitsgrad: Schwierigkeitsgrad;
    readonly referenztyp: Referenztyp;
    readonly referenz: string;
    readonly status: STATUS;
    readonly anzahlElemente: number;
};
