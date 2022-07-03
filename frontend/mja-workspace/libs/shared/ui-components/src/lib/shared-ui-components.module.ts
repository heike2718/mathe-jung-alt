import { NgModule } from '@angular/core';
import { MessageComponent } from './message/message.component';
import { SharedUtilMjaModule } from '@mja-workspace/shared/util-mja';
import { MaterialModule } from './material.module';
import { FormsModule } from '@angular/forms';
import { DeskriptorenFilterComponent } from './deskriptoren-filter/deskriptoren-filter.component';
import { AdminSuchfilterComponent } from './admin-suchfilter/admin-suchfilter.component';

@NgModule({
  imports: [FormsModule, SharedUtilMjaModule, MaterialModule],
  declarations: [
    MessageComponent,
    DeskriptorenFilterComponent,
    AdminSuchfilterComponent
  ],
  exports: [MessageComponent, FormsModule, MaterialModule, SharedUtilMjaModule],
})
export class SharedUiComponentsModule {}
