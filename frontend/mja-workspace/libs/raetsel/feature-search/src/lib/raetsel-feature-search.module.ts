import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselDomainModule } from '@mja-workspace/raetsel/domain';
import { SearchComponent } from './search.component';
import { HttpClientModule } from '@angular/common/http';
import { SuchfilterDomainModule } from '@mja-workspace/suchfilter/domain';
import { MaterialModule } from '@mja-workspace/shared/ui-components';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@mja-workspace/shared/auth/domain';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    RaetselDomainModule,
    SuchfilterDomainModule,
    MaterialModule,
    RouterModule.forChild([
      {
        path: 'raetsel',
        // canActivate: [AuthGuard],
        component: SearchComponent,
      }
    ]),
  ],
  declarations: [SearchComponent],
  exports: [SearchComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RaetselFeatureSearchModule {}
