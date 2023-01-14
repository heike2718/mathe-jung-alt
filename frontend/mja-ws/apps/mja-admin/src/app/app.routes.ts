import { Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";


export const appRoutes: Routes = [
  {
    path: '',
    // canActivate: [UserLoaderGuard],
    children: [
      {
        path: '',
        component: HomeComponent,
      },
      {
        path: 'raetsel',
        loadChildren: () =>
          import('@mja-ws/raetsel/feature').then((m) => m.raetselRoutes),
      }, 
      {
        path: 'raetselgruppen',
        loadChildren: () =>
          import('@mja-ws/raetselgruppen/feature').then((m) => m.raetselgruppenRoutes),
      },
    ],
  },
  {
    path: '**',
    component: HomeComponent
  }
];
