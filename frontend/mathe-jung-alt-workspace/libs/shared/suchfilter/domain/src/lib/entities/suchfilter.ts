import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";

export type SUCHKONTEXT = 'BILDER' | 'MEDIEN' | 'QUELLEN' | 'RAETSEL' | 'NOOP';

export interface Suchfilter {
    readonly kontext: SUCHKONTEXT;
    readonly suchstring: string;
    readonly deskriptoren: Deskriptor[];
};

export const initialSuchfilter: Suchfilter = {
    kontext: 'NOOP',
    suchstring: '',
    deskriptoren: []
};

