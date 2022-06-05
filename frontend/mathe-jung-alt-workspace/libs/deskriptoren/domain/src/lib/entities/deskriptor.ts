import { Suchkontext } from "@mathe-jung-alt-workspace/shared/suchfilter/domain";
import { MjaElementWithId, MjaSetUtils } from "@mathe-jung-alt-workspace/shared/utils";

export interface Deskriptor extends MjaElementWithId {
  name: string;
  admin: boolean;
  kontext: string;
};

export function getDifferenzmenge(alle: Deskriptor[], auszuschliessen: Deskriptor[]): Deskriptor[] {

  const setUtils: MjaSetUtils<Deskriptor> = new MjaSetUtils();
  const result: Deskriptor[] = setUtils.getDifferenzmenge(setUtils.toMjaEntityArray(alle), setUtils.toMjaEntityArray(auszuschliessen));
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
}
