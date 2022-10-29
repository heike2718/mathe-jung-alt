import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as fromRaetselgruppen from './+state/raetselgruppen.reducer';
import { StoreModule } from '@ngrx/store';
import { RaetselgruppenEffects } from './+state/raetselgruppen.effects';
import { EffectsModule } from '@ngrx/effects';
import { SharedUtilMjaModule } from '@mja-workspace/shared/util-mja';
import { SharedUiComponentsModule } from '@mja-workspace/shared/ui-components';

@NgModule({
  imports: [
    CommonModule,
    SharedUtilMjaModule,
    SharedUiComponentsModule,
    StoreModule.forFeature(
      fromRaetselgruppen.RAETSELGRUPPEN_FEATURE_KEY,
      fromRaetselgruppen.reducer
    ),
    EffectsModule.forFeature([RaetselgruppenEffects]),
  ],
})
export class RaetselgruppenDomainModule { }
