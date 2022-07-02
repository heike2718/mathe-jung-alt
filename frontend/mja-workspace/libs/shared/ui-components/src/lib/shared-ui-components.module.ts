import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessageComponent } from './message/message.component';
import { SharedUtilMjaModule } from '@mja-workspace/shared/util-mja';
import { MaterialModule } from './material.module';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    FormsModule,
    SharedUtilMjaModule, 
    MaterialModule],
  declarations: [MessageComponent],
  exports: [
    MessageComponent,
    FormsModule,
    MaterialModule,
    SharedUtilMjaModule
  ]
})
export class SharedUiComponentsModule {}
