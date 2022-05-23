import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselDomainModule } from '@mathe-jung-alt-workspace/raetsel/domain';
import { RaetselEditComponent } from './raetsel-edit.component';

@NgModule({
  imports: [CommonModule, RaetselDomainModule],
  declarations: [RaetselEditComponent],
  exports: [RaetselEditComponent],
})
export class FeatureRaetselEditModule {}
