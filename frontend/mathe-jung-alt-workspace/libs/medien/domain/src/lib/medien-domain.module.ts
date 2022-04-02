import * as fromMedien from './+state/medien/medien.reducer';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MedienEffects } from './+state/medien/medien.effects';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(fromMedien.MEDIEN_FEATURE_KEY, fromMedien.reducer),
    EffectsModule.forFeature([MedienEffects]),
  ],
})
export class MedienDomainModule {}
