import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { DeskriptorenDomainModule } from '@mathe-jung-alt-workspace/deskriptoren/domain';
import { MaterialModule } from './material.module';
import { AdminSuchfilterComponent } from './suchfilter/admin/admin-suchfilter.component';
import { DeskriptorenSearchComponent } from './suchfilter/feature-search/deskriptoren-search.component';

@NgModule({
  imports: [
    CommonModule,
    MaterialModule,
    DeskriptorenDomainModule
  ],
  declarations: [
    DeskriptorenSearchComponent,
    AdminSuchfilterComponent
  ],
  exports: [
    DeskriptorenSearchComponent,
    AdminSuchfilterComponent
  ],
  providers: [],
})
export class SharedUiComponentsModule { }
