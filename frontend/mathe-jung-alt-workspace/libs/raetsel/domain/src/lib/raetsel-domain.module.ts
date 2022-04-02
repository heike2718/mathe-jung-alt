import * as fromRaetsel from './+state/raetsel/raetsel.reducer';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselEffects } from './+state/raetsel/raetsel.effects';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(
      fromRaetsel.RAETSEL_FEATURE_KEY,
      fromRaetsel.reducer
    ),
    EffectsModule.forFeature([RaetselEffects]),
  ],
})
export class RaetselDomainModule {}
