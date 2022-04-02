import * as fromBild from './+state/bild/bild.reducer';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BildEffects } from './+state/bild/bild.effects';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(fromBild.BILD_FEATURE_KEY, fromBild.reducer),
    EffectsModule.forFeature([BildEffects]),
  ],
})
export class BilderDomainModule {}
