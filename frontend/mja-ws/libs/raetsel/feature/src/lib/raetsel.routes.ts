import { Routes } from "@angular/router";
import { RaetselSearchComponent } from "./raetsel-search/raetsel-search.component";
import { raetselDataProvider } from '@mja-ws/raetsel/api';
import { AdminGuard, AuthGuard } from '@mja-ws/shared/auth/api';
import { RaetselDetailsComponent } from "./raetsel-details/raetsel-details.component";
import { grafikDataProvider } from "@mja-ws/grafik/api";
import { RaetselEditorComponent } from "./raetsel-editor/raetsel-editor.component";


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
    path: 'editor',
    canActivate: [AdminGuard], // TODO: ReaetselDetailsLoadedGuard
    component: RaetselEditorComponent,
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
