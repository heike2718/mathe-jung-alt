import { Deskriptor } from "@mja-workspace/suchfilter/domain";

export type Quellenart = 'BUCH' | 'PERSON' | 'ZEITSCHRIFT';

export interface Quelle {
  readonly id: string;
  readonly sortNumber: number;
  readonly quellenart: Quellenart;
  readonly name: string;
  readonly mediumUuid?: string;
  readonly deskriptoren: Deskriptor[];
};