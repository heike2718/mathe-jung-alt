import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MedienDomainModule } from '@mathe-jung-alt-workspace/medien/domain';
import { SearchComponent } from './search.component';

@NgModule({
  imports: [CommonModule, MedienDomainModule],
  declarations: [SearchComponent],
  exports: [SearchComponent],
})
export class MedienFeatureSearchModule {}
