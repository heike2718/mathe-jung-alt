import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GrafikDomainModule } from '@mja-workspace/grafik/domain';
import { SearchComponent } from './search.component';
import { GrafikDetailsComponent } from './grafik-details/grafik-details.component';

@NgModule({
  imports: [CommonModule, GrafikDomainModule],
  declarations: [SearchComponent, GrafikDetailsComponent],
  exports: [SearchComponent,GrafikDetailsComponent],
})
export class GrafikFeatureSearchModule {}
