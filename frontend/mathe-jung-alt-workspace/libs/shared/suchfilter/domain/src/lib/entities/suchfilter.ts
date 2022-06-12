import { Deskriptor, filterByKontext } from "@mathe-jung-alt-workspace/deskriptoren/domain";

export type Suchkontext = 'BILDER' | 'MEDIEN' | 'QUELLEN' | 'RAETSEL' | 'NOOP';

export const QUERY_PARAM_SUCHSTRING = 'suchstring';
export const QUERY_PARAM_DESKRIPTOREN = 'deskriptoren';
export const QUERY_PARAM_TYPE_DESKRIPTOREN = 'typeDeskriptoren';
export const QUERY_PARAM_LIMIT = 'limit';
export const QUERY_PARAM_OFFSET = 'offset';
export const QUERY_PARAM_SORT_DIRECTION = 'sortDirection';



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

export interface SuchfilterUIModel {
    readonly suchfilter: Suchfilter;
    readonly filteredDeskriptoren: Deskriptor[]; // das sind die zum Kontext passenden Deskriptoren
    readonly changed: boolean;
};

export interface SuchfilterUndStatus {
    readonly suchfilter: Suchfilter | undefined;
    readonly nichtLeer: boolean
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

export function findSuchfilterUIModelWithKontext(kontext: Suchkontext, uiModels: SuchfilterUIModel[]): SuchfilterUIModel | undefined {

    const result = uiModels.filter(model => kontext === model.suchfilter.kontext);

    if (result.length === 1) {
        return result[0] as SuchfilterUIModel;
    }

    return undefined;
}

export function createInitialSuchfilterUIModels(deskriptoren: Deskriptor[]): SuchfilterUIModel[] {

    const result: SuchfilterUIModel[] = [];
    result.push({ suchfilter: initialSuchfilter, filteredDeskriptoren: [], changed: false });

    {
       const kontext: Suchkontext = 'BILDER';
       const suchfilter: Suchfilter = {
           kontext: kontext,
           suchstring: '',
           deskriptoren: []
       };
       const model: SuchfilterUIModel = {
         suchfilter: suchfilter,
         filteredDeskriptoren:  filterByKontext(kontext, deskriptoren),
         changed: false
       };
       result.push(model);
    }

    {
        const kontext: Suchkontext = 'MEDIEN';
        const suchfilter: Suchfilter = {
            kontext: kontext,
            suchstring: '',
            deskriptoren: []
        };
        const model: SuchfilterUIModel = {
          suchfilter: suchfilter,
          filteredDeskriptoren:  filterByKontext(kontext, deskriptoren),
          changed: false
        };
        result.push(model);
     }

     {
        const kontext: Suchkontext = 'QUELLEN';
        const suchfilter: Suchfilter = {
            kontext: kontext,
            suchstring: '',
            deskriptoren: []
        };
        const model: SuchfilterUIModel = {
          suchfilter: suchfilter,
          filteredDeskriptoren:  filterByKontext(kontext, deskriptoren),
          changed: false
        };
        result.push(model);
     }

     {
        const kontext: Suchkontext = 'RAETSEL';
        const suchfilter: Suchfilter = {
            kontext: kontext,
            suchstring: '',
            deskriptoren: []
        };
        const model: SuchfilterUIModel = {
          suchfilter: suchfilter,
          filteredDeskriptoren:  filterByKontext(kontext, deskriptoren),
          changed: false
        };
        result.push(model);
     }

    return result;
}
