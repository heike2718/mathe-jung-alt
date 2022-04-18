import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";

export type Suchkontext = 'BILDER' | 'MEDIEN' | 'QUELLEN' | 'RAETSEL' | 'NOOP';

export interface Suchfilter {
    readonly kontext: Suchkontext;
    readonly suchstring: string;
    readonly deskriptoren: Deskriptor[];
};

export const initialSuchfilter: Suchfilter = {
    kontext: 'NOOP',
    suchstring: '',
    deskriptoren: []
};

export interface SuchfilterWithStatus {
    readonly suchfilter: Suchfilter;
    readonly nichtLeer: boolean;
}

