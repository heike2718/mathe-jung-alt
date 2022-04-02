import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselDomainModule } from '@mathe-jung-alt-workspace/raetsel/domain';
import { SearchComponent } from './search.component';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule, 
    RaetselDomainModule
  ],
  declarations: [SearchComponent],
  exports: [SearchComponent],
})
export class RaetselFeatureSearchModule {}
