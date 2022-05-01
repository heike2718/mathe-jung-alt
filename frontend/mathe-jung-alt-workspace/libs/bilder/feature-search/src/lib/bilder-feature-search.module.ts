import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BilderDomainModule } from '@mathe-jung-alt-workspace/bilder/domain';
import { BilderSearchComponent } from './bilder-search.component';

@NgModule({
  imports: [CommonModule, BilderDomainModule],
  declarations: [BilderSearchComponent],
  exports: [BilderSearchComponent],
})
export class BilderFeatureSearchModule {}
