import { Routes } from "@angular/router";
import { RaetselSearchComponent } from "./raetsel-search/raetsel-search.component";
import { raetselDataProvider } from '@mja-ws/raetsel/api';
import { AuthGuard } from '@mja-ws/shared/auth/api';
import { RaetselDetailsComponent } from "./raetsel-details/raetsel-details.component";
import { grafikDataProvider } from "@mja-ws/grafik/api";


export const raetselRoutes: Routes = [
  {
    path: 'uebersicht',
    canActivate: [AuthGuard],
    component: RaetselSearchComponent,
    providers: [
      raetselDataProvider,
      grafikDataProvider
    ],
  },
  {
    path: 'details',
    canActivate: [AuthGuard],
    component: RaetselDetailsComponent,
    providers: [
      raetselDataProvider,
      grafikDataProvider
    ],
  },
  {
    path: '',
    pathMatch: 'full',
    canActivate: [AuthGuard],
    component: RaetselSearchComponent,
    providers: [
      raetselDataProvider,
      grafikDataProvider
    ],
  },
];
