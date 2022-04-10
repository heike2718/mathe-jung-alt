import * as fromDeskriptor from './+state/deskriptor/deskriptor.reducer';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DeskriptorEffects } from './+state/deskriptor/deskriptor.effects';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { SharedUtilsModule } from '@mathe-jung-alt-workspace/shared/utils';

@NgModule({
  imports: [
    CommonModule,
    SharedUtilsModule,
    StoreModule.forFeature(
      fromDeskriptor.DESKRIPTOR_FEATURE_KEY,
      fromDeskriptor.reducer
    ),
    EffectsModule.forFeature([DeskriptorEffects]),
  ],
})
export class DeskriptorenDomainModule {}
