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

export class SuchfilterQueryParameterMapper {

    #nameSuchstring = '?suchstring=';
    #nameDeskriptoren = '&deskriptoren=';

    constructor(private suchfilter: Suchfilter) { }

    /**
     * Mappt den suchfilter auf einen Query-Parameter ?suchstring=...&deskriptoren=...
     * @returns string
     */
    public apply(): string {

        let result = this.#nameSuchstring + this.suchfilter.suchstring;

        if (this.suchfilter.deskriptoren.length === 0) {
            return result;
        }

        result += this.#nameDeskriptoren;

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

