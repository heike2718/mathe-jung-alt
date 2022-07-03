import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as fromSuchfilter from './+state/suchfilter.reducer';
import { StoreModule } from '@ngrx/store';
import { SuchfilterEffects } from './+state/suchfilter.effects';
import { EffectsModule } from '@ngrx/effects';
import { SharedUtilMjaModule } from '@mja-workspace/shared/util-mja';

@NgModule({
  imports: [
    CommonModule,
    SharedUtilMjaModule,
    StoreModule.forFeature(
      fromSuchfilter.SUCHFILTER_FEATURE_KEY,
      fromSuchfilter.reducer
    ),
    EffectsModule.forFeature([SuchfilterEffects]),
  ],
})
export class SuchfilterDomainModule {}
