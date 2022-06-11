import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  RaetselDetailsGuard,
  RaetselDomainModule,
} from '@mathe-jung-alt-workspace/raetsel/domain';
import { RaetselSearchComponent } from './raetsel-search.component';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule, SharedUiMessagingModule } from '@mathe-jung-alt-workspace/shared/ui-components';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { SharedSuchfilterDomainModule } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { SharedSuchfilterComponentModule } from '@mathe-jung-alt-workspace/shared/suchfilter/suchfilter-component';
import { ReaetselDetailsComponent } from './reaetsel-details/reaetsel-details.component';
import { AntwortvorschlagComponent } from './reaetsel-details/antwortvorschlaege/antwortvorschlag.component';
import { FormsModule } from '@angular/forms';
import { RaetselUiModule } from '@mathe-jung-alt-workspace/raetsel/ui-raetsel';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    RaetselDomainModule,
    RaetselUiModule,
    SharedSuchfilterDomainModule,
    SharedSuchfilterComponentModule,
    SharedUiMessagingModule,
    MaterialModule,
    RouterModule.forChild([
      {
        path: 'raetsel',
        canActivate: [AuthGuard],
        component: RaetselSearchComponent,
      },
      {
        path: 'raetsel/details',
        canActivate: [AuthGuard, RaetselDetailsGuard],
        component: ReaetselDetailsComponent,
      },
    ]),
  ],
  declarations: [
    RaetselSearchComponent,
    ReaetselDetailsComponent,
    AntwortvorschlagComponent
  ],
  exports: [RaetselSearchComponent, RouterModule, AntwortvorschlagComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class RaetselSearchModule {}
