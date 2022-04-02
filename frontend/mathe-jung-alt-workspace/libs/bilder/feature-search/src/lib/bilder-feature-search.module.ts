import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BilderDomainModule } from '@mathe-jung-alt-workspace/bilder/domain';
import { SearchComponent } from './search.component';

@NgModule({
  imports: [CommonModule, BilderDomainModule],
  declarations: [SearchComponent],
  exports: [SearchComponent],
})
export class BilderFeatureSearchModule {}
