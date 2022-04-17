import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { AuthGuard } from "@mathe-jung-alt-workspace/shared/auth/domain";
import { MaterialModule } from "@mathe-jung-alt-workspace/shared/ui-components";
import { DashboardComponent } from "./dashboard.component";

@NgModule({
    imports: [CommonModule,
      MaterialModule,
      RouterModule.forChild([
        {
          path: 'dashboard',
          canActivate: [AuthGuard],
          component: DashboardComponent,
        },
      ]),
    ],
    declarations: [DashboardComponent],
    exports: [DashboardComponent],
  })
  export class DashboardComponentModule {
  
  }
  