import { Routes } from "@angular/router";
import { RaetselSearchComponent } from "./raetsel-search/raetsel-search.component";
import { raetselDataProvider } from '@mja-ws/raetsel/data';
import { AuthGuard } from '@mja-ws/shared/auth/api';


export const raetselRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: RaetselSearchComponent,
    providers: raetselDataProvider,
  },
];
