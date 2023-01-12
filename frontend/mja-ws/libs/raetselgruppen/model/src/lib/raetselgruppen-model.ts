import { Referenztyp, Schwierigkeitsgrad, SortOrder, STATUS } from '@mja-ws/core/model';

export interface RaetselgruppeBasisdaten {
  readonly id: string;
  readonly name: string | undefined;  // FIXME: warum auch undefined?
  readonly kommentar: string | undefined;
  readonly schwierigkeitsgrad: Schwierigkeitsgrad;
  readonly referenztyp: Referenztyp | undefined;
  readonly referenz: string | undefined;
  readonly status: STATUS;
  readonly geaendertDurch: string | undefined;
};