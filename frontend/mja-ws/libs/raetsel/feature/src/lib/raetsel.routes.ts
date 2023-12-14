import { Routes } from "@angular/router";
import { RaetselSearchComponent } from "./raetsel-search/raetsel-search.component";
import { raetselDataProvider } from '@mja-ws/raetsel/api';
import { AuthFacade } from '@mja-ws/core/api';
import { RaetselDetailsComponent } from "./raetsel-details/raetsel-details.component";
import { RaetselEditorComponent } from "./raetsel-editor/raetsel-editor.component";
import { inject } from "@angular/core";
import { embeddableImagesDataProvider } from "@mja-ws/embeddable-images/api";

// siehe https://www.angulararchitects.io/en/aktuelles/modern-and-lightweight-angular-architectures-with-angulars-latest-innovations/

export const raetselRoutes: Routes = [
  {
    path: 'uebersicht',
    canActivate: [() => inject(AuthFacade).userIsLoggedIn$],
    component: RaetselSearchComponent,
    providers: [
      raetselDataProvider,
      embeddableImagesDataProvider
    ],
  },
  {
    path: 'details',
    canActivate: [() => inject(AuthFacade).userIsLoggedIn$],
    component: RaetselDetailsComponent,
    providers: [
      raetselDataProvider,
      embeddableImagesDataProvider
    ],
  },
  {
    path: 'editor',
    canActivate: [() => inject(AuthFacade).userIsAdmin$], // TODO: ReaetselDetailsLoadedGuard
    component: RaetselEditorComponent,
    providers: [
      raetselDataProvider,
      embeddableImagesDataProvider
    ],
  },
  {
    path: '',
    pathMatch: 'full',
    canActivate: [() => inject(AuthFacade).userIsLoggedIn$],
    component: RaetselSearchComponent,
    providers: [
      raetselDataProvider,
      embeddableImagesDataProvider
    ],
  },
];
