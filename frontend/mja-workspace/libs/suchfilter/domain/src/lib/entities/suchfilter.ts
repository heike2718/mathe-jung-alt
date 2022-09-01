import { MjaElementWithId, MjaSetUtils } from "@mja-workspace/shared/util-mja";

export type Suchkontext = 'BILDER' | 'MEDIEN' | 'QUELLEN' | 'RAETSEL' | 'NOOP';

export const QUERY_PARAM_SUCHSTRING = 'suchstring';
export const QUERY_PARAM_DESKRIPTOREN = 'deskriptoren';
export const QUERY_PARAM_TYPE_DESKRIPTOREN = 'typeDeskriptoren';
export const QUERY_PARAM_LIMIT = 'limit';
export const QUERY_PARAM_OFFSET = 'offset';
export const QUERY_PARAM_SORT_DIRECTION = 'sortDirection';

export interface Deskriptor extends MjaElementWithId {
    name: string;
    admin: boolean;
    kontext: string; // kommaseparierte Suchkontexte
};

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
    sortDirection: string
};

export const initialPaginationState: PaginationState = {
    anzahlTreffer: 0,
    pageSize: 20,
    pageIndex: 0,
    sortDirection: 'asc'
};

export interface SuchfilterWithStatus {
    readonly suchfilter: Suchfilter;
    readonly nichtLeer: boolean;
};


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
};

export function findSuchfilterUIModelWithKontext(kontext: Suchkontext, uiModels: SuchfilterUIModel[]): SuchfilterUIModel | undefined {

    const result = uiModels.filter(model => kontext === model.suchfilter.kontext);

    if (result.length === 1) {
        return result[0] as SuchfilterUIModel;
    }

    return undefined;
};

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
            filteredDeskriptoren: filterByKontext(kontext, deskriptoren),
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
            filteredDeskriptoren: filterByKontext(kontext, deskriptoren),
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
            filteredDeskriptoren: filterByKontext(kontext, deskriptoren),
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
            filteredDeskriptoren: filterByKontext(kontext, deskriptoren),
            changed: false
        };
        result.push(model);
    }

    return result;
};

export function filterByKontext(kontext: Suchkontext, deskriptoren: Deskriptor[]): Deskriptor[] {

    let result: Deskriptor[] = [];

    switch (kontext) {
        case 'RAETSEL': result = deskriptoren.filter(d => d.kontext.indexOf('RAETSEL') > -1); break;
        case 'BILDER': result = deskriptoren.filter(d => d.kontext.indexOf('BILDER') > -1); break;
        case 'MEDIEN': result = deskriptoren.filter(d => d.kontext.indexOf('MEDIEN') > -1); break;
        case 'QUELLEN': result = deskriptoren.filter(d => d.kontext.indexOf('QUELLEN') > -1); break;
    }
    return result;
};

export function getDifferenzmenge(alle: Deskriptor[], auszuschliessen: Deskriptor[]): Deskriptor[] {

    const setUtils: MjaSetUtils<Deskriptor> = new MjaSetUtils();
    const result: Deskriptor[] = setUtils.getDifferenzmenge(setUtils.toMjaEntityArray(alle), setUtils.toMjaEntityArray(auszuschliessen));
    return result;
};

export function deskriptorenToString(deskriptoren: Deskriptor[]): string {

    if (deskriptoren.length === 0) {
        return '';
    }

    let result = '';

    for (let index = 0; index < deskriptoren.length; index++) {

        const deskriptor = deskriptoren[index];
        if (index < deskriptoren.length - 1) {
            result += deskriptor.name + ", ";
        } else {
            result += deskriptor.name;
        }

    }

    return result;
};

export function suchkriterienVorhanden(suchfilter: Suchfilter | undefined): boolean {

    if (!suchfilter || suchfilter.kontext === 'NOOP') {
        return false;
    }

    // Worte mit weniger als 4 Zeichen sind nicht Teil des Volltextindex. Daher erst fertig, wenn mindestens 4 Zeichen
    return suchfilter.suchstring.trim().length > 3 || suchfilter.deskriptoren.length > 0;
}


