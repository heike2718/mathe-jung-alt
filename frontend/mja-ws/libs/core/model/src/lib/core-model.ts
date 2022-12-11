
/** 
 * Ein angemeldeter ADMIN bzw. AUTOR ist selbst eine Quelle. Dies ist die zugeordnete Quelle.
 * Alle Raetsel und davon abgeleiteten Objekte referenzieren eine Quelle. Dies ist die zugeordnete Quelle 
*/
export interface QuelleUI {
  readonly id: string;
  readonly name: string;
};

export const noopQuelle: QuelleUI = {
  id: 'NOOP',
  name: ''
};
