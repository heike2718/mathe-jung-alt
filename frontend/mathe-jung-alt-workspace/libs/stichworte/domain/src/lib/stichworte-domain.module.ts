import * as fromStichwort from './+state/stichwort/stichwort.reducer';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StichwortEffects } from './+state/stichwort/stichwort.effects';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    StoreModule.forFeature(
      fromStichwort.STICHWORT_FEATURE_KEY,
      fromStichwort.reducer
    ),
    EffectsModule.forFeature([StichwortEffects]),
  ],
})
export class StichworteDomainModule {}
