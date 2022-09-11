import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MaterialModule,
  SharedUiComponentsModule,
} from '@mja-workspace/shared/ui-components';
import { SuchfilterDomainModule } from '@mja-workspace/suchfilter/domain';
import { RaetselgruppenDomainModule } from '@mja-workspace/raetselgruppen/domain';
import { RaetselgruppenSearchComponent } from './raetselgruppen-search/raetselgruppen-search.component';
import { RouterModule } from '@angular/router';
import { AdminGuard } from '@mja-workspace/shared/auth/domain';
import { RaetselgruppeDetailsComponent } from './raetselgruppe-details/raetselgruppe-details.component';

@NgModule({
  imports: [
    CommonModule,
    RaetselgruppenDomainModule,
    SharedUiComponentsModule,
    SuchfilterDomainModule,
    MaterialModule,
    RouterModule.forChild([
      {
        path: 'uebersicht',
        canActivate: [AdminGuard],
        component: RaetselgruppenSearchComponent,
      }, 
      {
        path: 'details',
        canActivate: [AdminGuard],
        component: RaetselgruppeDetailsComponent,
      },
    ]),
  ],
  declarations: [
    RaetselgruppenSearchComponent,
    RaetselgruppeDetailsComponent
  ],
  exports: [
    RaetselgruppenSearchComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class RaetselgruppenFeatureSearchModule { }
