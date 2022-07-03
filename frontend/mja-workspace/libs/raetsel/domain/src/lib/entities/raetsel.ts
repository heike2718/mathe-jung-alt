import { Deskriptor } from "@mja-workspace/suchfilter/domain";

export type LATEX_OUTPUTFORMAT = 'PDF' | 'PNG';
export type LATEX_LAYOUT_ANTWORTVORSCHLAEGE = 'ANKREUZTABELLE' | 'BUCHSTABEN' | 'DESCRIPTION' | 'NOOP';
export type STATUS = 'ERFASST' | 'FREIGEGEBEN';

export const anzeigeAntwortvorschlaegeSelectInput: string[] = [
  'ANKREUZTABELLE', 'BUCHSTABEN', 'DESCRIPTION', 'NOOP'
];

/** Minimalset an Attributen, die bei einer Suche geladen werden sollen */
export interface Raetsel {
  readonly id: string;
  readonly schluessel: string;
  readonly name: string;
  readonly status: STATUS;
  readonly kommentar?: string;
  readonly deskriptoren: Deskriptor[];
};