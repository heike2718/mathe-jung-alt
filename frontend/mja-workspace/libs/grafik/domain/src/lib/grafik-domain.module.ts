import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as fromGrafik from './+state/grafik.reducer';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { GrafikEffects } from './+state/grafik.effects';



@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(
      fromGrafik.GRAFIK_FEATURE_KEY,
      fromGrafik.reducer
    ),
    EffectsModule.forFeature([GrafikEffects]),
  ],
})
export class GrafikDomainModule { }
