import { MjaElementWithId } from "@mathe-jung-alt-workspace/shared/utils";

export interface Deskriptor extends MjaElementWithId {
  name: string;
  admin: boolean;
  kontext: string; 
}
