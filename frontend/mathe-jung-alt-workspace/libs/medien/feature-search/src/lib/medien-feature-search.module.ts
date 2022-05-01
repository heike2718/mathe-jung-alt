import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MedienDomainModule } from '@mathe-jung-alt-workspace/medien/domain';
import { MedienSearchComponent } from './medien-search.component';

@NgModule({
  imports: [CommonModule, MedienDomainModule],
  declarations: [MedienSearchComponent],
  exports: [MedienSearchComponent],
})
export class MedienFeatureSearchModule {}
