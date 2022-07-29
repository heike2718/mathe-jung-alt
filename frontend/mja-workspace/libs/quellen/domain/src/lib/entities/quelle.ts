import { Deskriptor } from "@mja-workspace/suchfilter/domain";

export const STORAGE_KEY_QUELLE = 'quelle';

export type Quellenart = 'BUCH' | 'PERSON' | 'ZEITSCHRIFT';

export interface Quelle {
  readonly id: string;
  readonly sortNumber: number;
  readonly quellenart: Quellenart;
  readonly name: string;
  readonly mediumUuid?: string;
  readonly deskriptoren: Deskriptor[];
};