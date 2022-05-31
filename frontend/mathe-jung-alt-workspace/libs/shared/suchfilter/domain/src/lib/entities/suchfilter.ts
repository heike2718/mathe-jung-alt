import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";

export type Suchkontext = 'BILDER' | 'MEDIEN' | 'QUELLEN' | 'RAETSEL' | 'NOOP';

export interface PageDefinition {
    pageSize: number,
    pageIndex: number,
    sortDirection: string
};

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


export interface PaginationState {
    anzahlTreffer: number;
    pageSize: number,
    pageIndex: number,
    sortDirection: string,
    suchfilter: Suchfilter
};

export const initialPaginationState: PaginationState = {
    anzahlTreffer: 0,
    pageSize: 10,
    pageIndex: 0,
    sortDirection: 'asc',
    suchfilter: initialSuchfilter
};

export interface SuchfilterWithStatus {
    readonly suchfilter: Suchfilter;
    readonly nichtLeer: boolean;
}

export const QUERY_PARAM_SUCHSTRING = 'suchstring';
export const QUERY_PARAM_DESKRIPTOREN = 'deskriptoren';
export const QUERY_PARAM_LIMIT = 'limit';
export const QUERY_PARAM_OFFSET = 'offset';
export const QUERY_PARAM_SORT_DIRECTION = 'sortDirection';



export class SuchfilterQueryParameterMapper {

    constructor(private suchfilter: Suchfilter) { }

    getDeskriptoren(): string {

        let result = '';
        for (let index = 0; index < this.suchfilter.deskriptoren.length; index++) {

            const deskriptor: Deskriptor = this.suchfilter.deskriptoren[index];

            if (index < this.suchfilter.deskriptoren.length - 1) {
                result += deskriptor.id + ',';
            } else {
                result += deskriptor.id
            }
        }
        return result;
    }
}

