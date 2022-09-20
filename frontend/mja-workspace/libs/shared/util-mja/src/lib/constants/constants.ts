export type STATUS = 'ERFASST' | 'FREIGEGEBEN';

export type SortOrder = 'asc' | 'desc';

export type Referenztyp = 'NOOP' | 'MINIKAENGURU' | 'SERIE';

/** Referenztypen sinfd der Kontext zur Interpretation einer raetselgruppe.referenz auf das alte Aufgabenarchiv */
export interface GuiRaetselgruppeReferenztyp {
    readonly id: Referenztyp;
    readonly label: string;
};

export const initialGuiRaetselgruppeReferenztyp: GuiRaetselgruppeReferenztyp = { id: 'NOOP', label: '' };

export function getGuiRaetselgruppeReferenztypen(): GuiRaetselgruppeReferenztyp[] {
    return [
        initialGuiRaetselgruppeReferenztyp,
        {
            id: 'MINIKAENGURU',
            label: 'Minikänguru'
        },
        {
            id: 'SERIE',
            label: 'Serie'
        }
    ]
}

export function raetselgruppeReferenztypOfLabel(label: string): Referenztyp {

    const values = getGuiRaetselgruppeReferenztypen();
    const filteredValues: GuiRaetselgruppeReferenztyp[] = values.filter(sg => label === sg.label);
    if (filteredValues.length === 1) {
        return filteredValues[0].id;
    }

    return 'NOOP';
}

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

export interface GuiSchwierigkeitsgrad {
    readonly id: Schwierigkeitsgrad;
    readonly label: string;
};

export const initialSchwierigkeitsgrad: GuiSchwierigkeitsgrad = { id: 'NOOP', label: '' };

export function getSchwierigkeitsgrade(): GuiSchwierigkeitsgrad[] {

    const result: GuiSchwierigkeitsgrad[] = [
        initialSchwierigkeitsgrad,
        { id: 'IKID', label: 'Inklusion' },
        { id: 'EINS', label: 'Klasse 1' },
        { id: 'ZWEI', label: 'Klasse 2' },
        { id: 'EINS_ZWEI', label: 'Klassen 1/2' },
        { id: 'DREI_VIER', label: 'Klassen 3/4' },
        { id: 'FUENF_SECHS', label: 'Klassen 5/6' },
        { id: 'SIEBEN_ACHT', label: 'Klassen 7/8' },
        { id: 'AB_NEUN', label: 'ab Klasse 9' },
        { id: 'VORSCHULE', label: 'Vorschule' },
        { id: 'GRUNDSCHULE', label: 'Grundschule' },
        { id: 'SEK_1', label: 'Sekundarstufe 1' },
        { id: 'SEK_2', label: 'Sekundarstufe 2' }
    ]
    return result;
};


export function schwierigkeitsgradValueOfLabel(label: string): Schwierigkeitsgrad {

    const values = getSchwierigkeitsgrade();
    const filteredValues: GuiSchwierigkeitsgrad[] = values.filter(sg => label === sg.label);
    if (filteredValues.length === 1) {
        return filteredValues[0].id;
    }

    return 'NOOP';
}
