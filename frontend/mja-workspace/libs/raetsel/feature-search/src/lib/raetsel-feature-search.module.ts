import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselDomainModule } from '@mja-workspace/raetsel/domain';
import { RaetselSearchComponent } from './raetsel-search.component';
import { HttpClientModule } from '@angular/common/http';
import { SuchfilterDomainModule } from '@mja-workspace/suchfilter/domain';
import { SharedUiComponentsModule } from '@mja-workspace/shared/ui-components';
import { MaterialModule } from '@mja-workspace/shared/ui-components';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@mja-workspace/shared/auth/domain';
import { RaetselDetailsComponent } from './raetsel-details/raetsel-details.component';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    RaetselDomainModule,
    SuchfilterDomainModule,
    SharedUiComponentsModule,
    MaterialModule,
    RouterModule.forChild([
      {
        path: 'uebersicht',
        canActivate: [AuthGuard],
        component: RaetselSearchComponent,
      },
      {
        path: 'details',
        canActivate: [AuthGuard],
        component: RaetselDetailsComponent,
      },
      {
        path: '',
        pathMatch: 'full',
        canActivate: [AuthGuard],
        component: RaetselSearchComponent,
      },
    ]),
  ],
  declarations: [RaetselSearchComponent, RaetselDetailsComponent],
  exports: [RaetselSearchComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class RaetselFeatureSearchModule {}
