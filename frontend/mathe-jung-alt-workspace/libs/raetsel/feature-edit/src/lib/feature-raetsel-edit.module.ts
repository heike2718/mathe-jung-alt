import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  RaetselDetailsGuard,
  RaetselDomainModule,
} from '@mathe-jung-alt-workspace/raetsel/domain';
import { RaetselEditComponent } from './raetsel-edit.component';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedUiComponentsModule } from 'libs/shared/ui-components/src/lib/shared-ui-components.module';
import { RaetselUiModule } from '@mathe-jung-alt-workspace/raetsel/ui-raetsel';
@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    RaetselDomainModule,
    RaetselUiModule,
    SharedUiComponentsModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule.forChild([
      {
        path: 'raetseleditor',
        canActivate: [AuthGuard, RaetselDetailsGuard],
        component: RaetselEditComponent,
      },
    ]),
  ],
  declarations: [
    RaetselEditComponent
  ],
  exports: [RaetselEditComponent],
})
export class RaetselEditModule { }
