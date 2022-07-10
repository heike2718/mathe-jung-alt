import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselDetailsGuard, RaetselDomainModule } from '@mja-workspace/raetsel/domain';
import { RaetselEditComponent } from './raetsel-edit.component';
import { SharedUiRaetselModule } from '@mja-workspace/shared/ui-raetsel';
import { MaterialModule, SharedUiComponentsModule } from '@mja-workspace/shared/ui-components';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AdminGuard, AuthGuard } from '@mja-workspace/shared/auth/domain';

@NgModule({
  imports: [
    CommonModule,
    RaetselDomainModule,
    SharedUiRaetselModule,
    SharedUiComponentsModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule.forChild([
      {
        path: '',
        canActivate: [AdminGuard, RaetselDetailsGuard],
        component: RaetselEditComponent,
      },
    ]),
  ],
  declarations: [RaetselEditComponent],
  exports: [RaetselEditComponent],
})
export class RaetselFeatureEditModule {}
