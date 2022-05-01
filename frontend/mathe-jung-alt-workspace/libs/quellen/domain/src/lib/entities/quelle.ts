import { Deskriptor } from "@mathe-jung-alt-workspace/deskriptoren/domain";

export type Quellenart = 'BUCH' | 'PERSON' | 'ZEITSCHRIFT';

export interface Quelle {
  readonly uuid: string;
  readonly sortNumber: number;
  readonly quellenart: Quellenart;
  readonly name: string;
  readonly mediumUuid?: string;
  readonly deskriptoren: Deskriptor[];
};
