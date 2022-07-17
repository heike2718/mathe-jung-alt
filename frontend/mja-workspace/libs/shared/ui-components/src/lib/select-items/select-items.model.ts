import { SelectableItem } from "@mja-workspace/shared/util-mja";

export interface SelectItemsCompomentModel {
    ueberschriftAuswahlliste: string;
    ueberschriftGewaehlteItems: string;
    vorrat: SelectableItem[];
    gewaehlteItems: SelectableItem[];
};

export const initialSelectItemsComponentModel: SelectItemsCompomentModel = {
    ueberschriftAuswahlliste: '',
    ueberschriftGewaehlteItems: 'gewählt:',
    vorrat: [],
    gewaehlteItems: []
};