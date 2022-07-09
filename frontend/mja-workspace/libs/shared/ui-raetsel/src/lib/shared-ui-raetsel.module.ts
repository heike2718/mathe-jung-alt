import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselImagesComponent } from './raetsel-image/raetsel-images.component';
import { PrintRaetselDialogComponent } from './print-raetsel-dialog/print-raetsel-dialog.component';
import { MaterialModule } from '@mja-workspace/shared/ui-components';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [CommonModule,MaterialModule, FormsModule],
  declarations: [RaetselImagesComponent, PrintRaetselDialogComponent],
  exports: [RaetselImagesComponent],
})
export class SharedUiRaetselModule {}
