import * as fromDeskriptor from './+state/deskriptor/deskriptor.reducer';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DeskriptorEffects } from './+state/deskriptor/deskriptor.effects';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(
      fromDeskriptor.DESKRIPTOR_FEATURE_KEY,
      fromDeskriptor.reducer
    ),
    EffectsModule.forFeature([DeskriptorEffects]),
  ],
})
export class DeskriptorenDomainModule {}
