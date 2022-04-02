import * as fromQuelle from './+state/quelle/quelle.reducer';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QuelleEffects } from './+state/quelle/quelle.effects';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(fromQuelle.QUELLE_FEATURE_KEY, fromQuelle.reducer),
    EffectsModule.forFeature([QuelleEffects]),
  ],
})
export class QuellenDomainModule {}
