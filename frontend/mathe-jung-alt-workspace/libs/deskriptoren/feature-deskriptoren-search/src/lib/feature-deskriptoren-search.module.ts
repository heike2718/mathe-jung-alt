import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DeskriptorenDomainModule } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { DeskriptorenSearchComponent } from './deskriptoren-search.component';

@NgModule({
  imports: [CommonModule, DeskriptorenDomainModule],
  declarations: [DeskriptorenSearchComponent],
  exports: [DeskriptorenSearchComponent],
})
export class DeskriptorenSearchModule {}
