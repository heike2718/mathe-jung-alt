import { LATEX_LAYOUT_ANTWORTVORSCHLAEGE } from "@mathe-jung-alt-workspace/raetsel/domain";

export interface DialogData {
    titel: string;
    layoutsAntwortvorschlaegeInput: string[];
    selectedLayoutAntwortvorschlaege?: string;
};