import { Message } from "@mja-workspace/shared/util-mja";
import { createAction, props } from "@ngrx/store";
import { GrafikSearchResult } from "../entities/grafik.model";

export const pruefeGrafik = createAction(
    '[Grafik] pruefeGrafik',
  props<{ pfad: string }>());


export const grafikGeprueft = createAction(
    '[Grafik] grafikGeprueft',
    props<{grafikSearchResult: GrafikSearchResult}>()
);

export const grafikHochgeladen = createAction(
  '[Grafik] grafik hochgeladen',
  props<{message: Message}>()
);

export const clearVorschau = createAction(
  '[Grafik] clearVorschau'
);
