import { createAction, props } from "@ngrx/store";
import { GrafikSearchResult } from "../entities/grafik.model";

export const pruefeGrafik = createAction(
    '[Grafik] pruefeGrafik',
  props<{ pfad: string }>());


export const grafikGeprueft = createAction(
    '[Grafik] grafikGeprueft',
    props<{grafikSearchResult: GrafikSearchResult}>()
);