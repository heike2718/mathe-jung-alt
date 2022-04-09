import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselDomainModule } from '@mathe-jung-alt-workspace/raetsel/domain';
import { SearchComponent } from './search.component';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';



@NgModule({
  imports: [
    CommonModule,
    HttpClientModule, 
    RaetselDomainModule,
    MaterialModule
  ],
  declarations: [SearchComponent],
  exports: [SearchComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RaetselFeatureSearchModule {}
