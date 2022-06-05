

export interface SelectableItem {
    readonly id: string | number;
    readonly name: string;
    readonly selected: boolean;
};


export interface SelectableItemsComponentModel {
    readonly vorrat: SelectableItem[];
    readonly auswahl: SelectableItem[];
};


