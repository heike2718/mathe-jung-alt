import { MjaElementWithId } from "@mathe-jung-alt-workspace/shared/utils";

export type Deskriptorkategorie = 'ART' | 'GRUPPE' | 'HERKUNFT' | 'STUFE' | 'THEMA';

export interface Deskriptor extends MjaElementWithId {
  name: string;
  admin: boolean;
  kontext: string;
  kategorie: Deskriptorkategorie;  
}
