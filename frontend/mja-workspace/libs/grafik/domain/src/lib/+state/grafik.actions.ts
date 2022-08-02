import { Message } from "@mja-workspace/shared/util-mja";
import { createAction, props } from "@ngrx/store";

export const pruefeGrafik = createAction(
    '[Grafik] pruefeGrafik',
  props<{ pfad: string }>());


export const grafikGeprueft = createAction(
    '[Grafik] grafikGeprueft',
    props<{messagePayload: Message}>()
);