import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DeskriptorenDomainModule } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { DeskriptorenSearchComponent } from './deskriptoren-search.component';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';

@NgModule({
  imports: [
    CommonModule,
    DeskriptorenDomainModule,
    MaterialModule
  ],
  declarations: [DeskriptorenSearchComponent],
  exports: [DeskriptorenSearchComponent],
})
export class DeskriptorenSearchModule {}
