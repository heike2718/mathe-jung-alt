import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StichworteDomainModule } from '@mathe-jung-alt-workspace/stichworte/domain';
import { StichwortsucheComponent } from './stichwortsuche.component';

@NgModule({
  imports: [
    CommonModule, 
    StichworteDomainModule
  ],
  declarations: [
    StichwortsucheComponent
  ],
  exports: [
    StichwortsucheComponent
  ],
})
export class StichworteFeatureStichwortsucheModule {}
