export type STATUS = 'ERFASST' | 'FREIGEGEBEN';

export type SortOrder = 'asc' | 'desc';

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

export type Referenztyp = 'NOOP' | 'MINIKAENGURU' | 'SERIE';

export type Schwierigkeitsgrad =
    'NOOP' |
    'AB_NEUN' |
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
export interface GuiRefereztyp {
    readonly id: Referenztyp;
    readonly label: string;
};

export const initialGuiReferenztyp: GuiRefereztyp = { id: 'NOOP', label: '' };

export class GuiReferenztypenMap {

    #referenztypen: Map<Referenztyp, string> = new Map();
    #referenztypenInvers: Map<string, Referenztyp> = new Map();

    constructor() {
        this.#referenztypen.set('NOOP', '');
        this.#referenztypen.set('MINIKAENGURU', 'Minikänguru');
        this.#referenztypen.set('SERIE', 'Serie');

        this.#referenztypenInvers.set('','NOOP');
        this.#referenztypenInvers.set('Minikänguru','MINIKAENGURU');
        this.#referenztypenInvers.set('Serie','SERIE');
    }

    public getReferenztypOfLabel(label: string): Referenztyp {

        const value = this.#referenztypenInvers.get(label);
        return value ? value : 'NOOP';
    }

    public getGuiRefereztyp(refTyp: Referenztyp): GuiRefereztyp {

        if (this.#referenztypen.has(refTyp)) {
            const label = this.#referenztypen.get(refTyp);

            if (label) {
                return { id: refTyp, label: label };
            } else {
                return initialGuiReferenztyp;
            }
            
        }

        return initialGuiReferenztyp;
    }

    public toGuiArray(): GuiRefereztyp[] {

        const result: GuiRefereztyp[] = [];
        this.#referenztypen.forEach((l: string, key: Referenztyp, _map: Map<Referenztyp, string>) => {
            result.push({ id: key, label: l });
        });

        return result;
    }

    public getLabelsSorted(): string[] {

        const result: string[] = [];

        this.toGuiArray().forEach(element => result.push(element.label));
        return result;
    }


};

export interface GuiSchwierigkeitsgrad {
    readonly id: Schwierigkeitsgrad;
    readonly label: string;
};

export const initialGuiSchwierigkeitsgrad: GuiSchwierigkeitsgrad = { id: 'NOOP', label: '' };

export class GuiSchwierigkeitsgradeMap {

    #schwierigkeitsgrade: Map<Schwierigkeitsgrad, string> = new Map();
    #schwierigkeitsgradeInvers: Map<string, Schwierigkeitsgrad> = new Map();

    constructor() {
        this.#schwierigkeitsgrade.set('NOOP', '');
        this.#schwierigkeitsgrade.set('IKID', 'Inklusion');
        this.#schwierigkeitsgrade.set('EINS', 'Klasse 1');
        this.#schwierigkeitsgrade.set('ZWEI', 'Klasse 2');
        this.#schwierigkeitsgrade.set('EINS_ZWEI', 'Klassen 1/2');
        this.#schwierigkeitsgrade.set('DREI_VIER', 'Klassen 3/4');
        this.#schwierigkeitsgrade.set('FUENF_SECHS', 'Klassen 5/6');
        this.#schwierigkeitsgrade.set('SIEBEN_ACHT', 'Klassen 7/8');
        this.#schwierigkeitsgrade.set('AB_NEUN', 'ab Klasse 9');
        this.#schwierigkeitsgrade.set('VORSCHULE', 'Vorschule');
        this.#schwierigkeitsgrade.set('GRUNDSCHULE', 'Grundschule');
        this.#schwierigkeitsgrade.set('SEK_1', 'Sekundarstufe 1');
        this.#schwierigkeitsgrade.set('SEK_2', 'Sekundarstufe 2');

        this.#schwierigkeitsgradeInvers.set('', 'NOOP');
        this.#schwierigkeitsgradeInvers.set('Inklusion', 'IKID');
        this.#schwierigkeitsgradeInvers.set('Klasse 1', 'EINS');
        this.#schwierigkeitsgradeInvers.set('Klasse 2', 'ZWEI');
        this.#schwierigkeitsgradeInvers.set('Klassen 1/2', 'EINS_ZWEI');
        this.#schwierigkeitsgradeInvers.set('Klassen 3/4','DREI_VIER');
        this.#schwierigkeitsgradeInvers.set('Klassen 5/6','FUENF_SECHS');
        this.#schwierigkeitsgradeInvers.set('Klassen 7/8','SIEBEN_ACHT');
        this.#schwierigkeitsgradeInvers.set('ab Klasse 9','AB_NEUN');
        this.#schwierigkeitsgradeInvers.set('Vorschule','VORSCHULE');
        this.#schwierigkeitsgradeInvers.set('Grundschule','GRUNDSCHULE');
        this.#schwierigkeitsgradeInvers.set('Sekundarstufe 1','SEK_1');
        this.#schwierigkeitsgradeInvers.set('Sekundarstufe 2','SEK_2');
    }

    public getSchwierigkeitsgradOfLabel(label: string): Schwierigkeitsgrad {

        const value: Schwierigkeitsgrad | undefined = this.#schwierigkeitsgradeInvers.get(label);
        return value ? value : 'NOOP';
    }

    public getGuiSchwierigkeitsgrade(refTyp: Schwierigkeitsgrad): GuiSchwierigkeitsgrad {

        if (this.#schwierigkeitsgrade.has(refTyp)) {

            const label = this.#schwierigkeitsgrade.get(refTyp);

            if (label) {
                return { id: refTyp, label: label };
            } else {
                return initialGuiSchwierigkeitsgrad;
            }
        }

        return initialGuiSchwierigkeitsgrad;
    }

    public toGuiArray(): GuiSchwierigkeitsgrad[] {

        const result: GuiSchwierigkeitsgrad[] = [];
        this.#schwierigkeitsgrade.forEach((l: string, key: Schwierigkeitsgrad, _map: Map<Schwierigkeitsgrad, string>) => {
            result.push({ id: key, label: l });
        });

        return result;
    }

    public getLabelsSorted(): string[] {

        const result: string[] = [];

        this.toGuiArray().forEach(element => result.push(element.label));
        return result;
    }
}


