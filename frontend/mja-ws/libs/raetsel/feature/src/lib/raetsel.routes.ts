import { Routes } from "@angular/router";
import { RaetselSearchComponent } from "./raetsel-search/raetsel-search.component";
import { raetselDataProvider } from '@mja-ws/raetsel/data';
import { AuthGuard } from '@mja-ws/shared/auth/api';
import { RaetselDetailsComponent } from "./raetsel-details/raetsel-details.component";


export const raetselRoutes: Routes = [
  {
    path: 'uebersicht',
    canActivate: [AuthGuard],
    component: RaetselSearchComponent,
    providers: raetselDataProvider,
  },
  {
    path: 'details',
    canActivate: [AuthGuard],
    component: RaetselDetailsComponent,
    providers: raetselDataProvider,
  },
  {
    path: '',
    pathMatch: 'full',
    canActivate: [AuthGuard],
    component: RaetselSearchComponent,
    providers: raetselDataProvider,
  },
];
