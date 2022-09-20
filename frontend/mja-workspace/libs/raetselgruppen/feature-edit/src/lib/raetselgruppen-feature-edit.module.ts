import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselgruppeDetailsGuard, RaetselgruppenDomainModule } from '@mja-workspace/raetselgruppen/domain';
import { RaetselgruppeEditComponent } from './raetselgruppe-edit.component';
import { MaterialModule } from '@mja-workspace/shared/ui-components';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AdminGuard } from '@mja-workspace/shared/auth/domain';

@NgModule({
  imports: [
    CommonModule,
    RaetselgruppenDomainModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule.forChild([
      {
        path: '',
        canActivate: [AdminGuard, RaetselgruppeDetailsGuard],
        component: RaetselgruppeEditComponent,
      },
    ]),
  ],
  declarations: [
    RaetselgruppeEditComponent
  ],
  exports: [
    RaetselgruppeEditComponent
  ],
})
export class RaetselgruppenFeatureEditModule {}
