export type OUTPUTFORMAT = 'PDF' | 'PNG' | 'LATEX';
export type LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'ANKREUZTABELLE' | 'BUCHSTABEN' | 'DESCRIPTION' | 'NOOP';

export const anzeigeAntwortvorschlaegeSelectInput: string[] = [
    'ANKREUZTABELLE', 'BUCHSTABEN', 'DESCRIPTION', 'NOOP'
];

export interface GeneratedFile {
    readonly fileName: string,
    readonly fileData: Blob
};

export interface SelectPrintparametersDialogData {
    titel: string;
    layoutsAntwortvorschlaegeInput: string[];
    selectedLayoutAntwortvorschlaege?: string;
};
