import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MaterialModule } from './material.module';
import { SelectItemsComponent } from './select-items/select-items.component';

@NgModule({
  imports: [
    CommonModule,
    MaterialModule
  ],
  declarations: [
    SelectItemsComponent
  ],
  exports: [
    SelectItemsComponent
  ],
  providers: [],
})
export class SharedUiComponentsModule { }
