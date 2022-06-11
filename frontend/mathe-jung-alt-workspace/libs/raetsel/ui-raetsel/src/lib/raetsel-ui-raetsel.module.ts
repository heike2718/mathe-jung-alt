import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselImagesComponent } from './raetsel-images/raetsel-images.component';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';
import { PrintRaetselDialogComponent } from './print-raetsel-dialog/print-raetsel-dialog.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    MaterialModule
  ],
  declarations: [
    RaetselImagesComponent,
    PrintRaetselDialogComponent
  ],
  exports: [
    RaetselImagesComponent
  ]
})
export class RaetselUiModule {}
