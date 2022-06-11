import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselImagesComponent } from './raetsel-images/raetsel-images.component';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';

@NgModule({
  imports: [
    CommonModule,
    MaterialModule
  ],
  declarations: [
    RaetselImagesComponent
  ],
  exports: [
    RaetselImagesComponent
  ]
})
export class RaetselUiModule {}
