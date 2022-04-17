import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselDomainModule } from '@mathe-jung-alt-workspace/raetsel/domain';
import { RaetselSearchComponent } from './raetsel-search.component';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@mathe-jung-alt-workspace/shared/auth/domain';



@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    RaetselDomainModule,
    MaterialModule,
    RouterModule.forChild([
      {
        path: 'raetsel',
        canActivate: [AuthGuard],
        component: RaetselSearchComponent,
      },
    ]),
  ],
  declarations: [RaetselSearchComponent],
  exports: [RaetselSearchComponent, RouterModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RaetselFeatureSearchModule { }
