import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule, SharedUiComponentsModule } from '@mja-workspace/shared/ui-components';
import { SuchfilterDomainModule } from '@mja-workspace/suchfilter/domain';
import { SammlungenDomainModule } from '@mja-workspace/sammlungen/domain';
import { AdminGuard } from '@mja-workspace/shared/auth/domain';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    SammlungenDomainModule,
    SharedUiComponentsModule,
    SuchfilterDomainModule,
    MaterialModule,
    // RouterModule.forChild([
    //   {
    //     path: 'uebersicht',
    //     canActivate: [AdminGuard],
    //     component: QuellenSearchComponent,
    //   }
    // ]),    
  ],
  declarations: [
    // QuellenSearchComponent
  ],
  exports: [
    // QuellenSearchComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SammlungenFeatureSearchModule {}
