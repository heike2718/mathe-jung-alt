import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QuellenDomainModule } from '@mathe-jung-alt-workspace/quellen/domain';
import { SearchComponent } from './search.component';

@NgModule({
  imports: [CommonModule, QuellenDomainModule],
  declarations: [SearchComponent],
  exports: [SearchComponent],
})
export class QuellenFeatureSearchModule {}
