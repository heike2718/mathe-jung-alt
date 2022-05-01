import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QuellenDomainModule } from '@mathe-jung-alt-workspace/quellen/domain';
import { QuellenSearchComponent } from './quellen-search.component';

@NgModule({
  imports: [CommonModule, QuellenDomainModule],
  declarations: [QuellenSearchComponent],
  exports: [QuellenSearchComponent],
})
export class QuellenFeatureSearchModule {}
