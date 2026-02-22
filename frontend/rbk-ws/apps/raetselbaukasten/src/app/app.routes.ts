import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

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
        loadChildren: () => import('@rbk-ws/raetsel/feature').then(m => m.raetselRoutes),
      },
      {
        path: 'aufgabensammlungen',
        loadChildren: () => import('@rbk-ws/aufgabensammlungen/feature').then(m => m.aufgabensammlungenRoutes),
      },
      {
        path: 'medien',
        loadChildren: () => import('@rbk-ws/medien/feature').then(m => m.medienRoutes),
      },
    ],
  },
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: '**',
    component: HomeComponent,
  },
];
