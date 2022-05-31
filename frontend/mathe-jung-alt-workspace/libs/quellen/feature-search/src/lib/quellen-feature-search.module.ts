import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QuelleDetailsGuard, QuellenDomainModule } from '@mathe-jung-alt-workspace/quellen/domain';
import { QuellenSearchComponent } from './quellen-search.component';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { SharedSuchfilterDomainModule } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { SharedSuchfilterComponentModule } from '@mathe-jung-alt-workspace/shared/suchfilter/suchfilter-component';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';
import { AdminGuard } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { QuelleDetailsComponent } from './quelle-details/quelle-details.component';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    QuellenDomainModule,
    SharedSuchfilterDomainModule,
    SharedSuchfilterComponentModule,
    MaterialModule,
    RouterModule.forChild([
      {
        path: 'quellen',
        canActivate: [AdminGuard],
        component: QuellenSearchComponent,
      }
    ]),
  ],
  declarations: [QuellenSearchComponent, QuelleDetailsComponent],
  exports: [QuellenSearchComponent, RouterModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class QuellenFeatureSearchModule {}
