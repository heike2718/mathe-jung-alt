import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";

export type Quellenart = 'BUCH' | 'PERSON' | 'ZEITSCHRIFT';

export interface Quelle {
  readonly id: string;
  readonly sortNumber: number;
  readonly quellenart: Quellenart;
  readonly name: string;
  readonly mediumUuid?: string;
  readonly deskriptoren: Deskriptor[];
};

export function deskriptorenToString(deskriptoren: Deskriptor[]): string {

  if (deskriptoren.length === 0) {
    return '';
  }

  let result = '';

  for (let index = 0; index < deskriptoren.length; index++) {

    const deskriptor = deskriptoren[index];
    if (index < deskriptoren.length -2) {
      result += deskriptor.name + ",";
    } else {
      result += deskriptor.name;
    }

  }

  return result;
}
