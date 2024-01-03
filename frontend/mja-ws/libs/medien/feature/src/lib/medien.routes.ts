import { inject } from "@angular/core";
import { Routes } from "@angular/router";
import { AuthFacade } from '@mja-ws/core/api';
import { MedienSearchComponent } from "./medien-search/medien-search.component";
import { medienDataProvider } from "@mja-ws/medien/api";
import { MediumEditComponent } from "./medium-edit/medium-edit.component";
import { MediumDetailsComponent } from "./medium-details/medium-details.component";


export const medienRoutes: Routes = [
  {
    path: 'uebersicht',
    canActivate: [() => inject(AuthFacade).userIsAdmin$],
    component: MedienSearchComponent,
    providers: [
      medienDataProvider
    ]
  }, {
    path: 'details',
    canActivate: [() => inject(AuthFacade).userIsAdmin$],
    component: MediumDetailsComponent
  },
  {
    path: 'editor',
    canActivate: [() => inject(AuthFacade).userIsAdmin$],
    component: MediumEditComponent
  },
  {
    path: '',
    pathMatch: 'full',
    canActivate: [() => inject(AuthFacade).userIsAdmin$],
    component: MedienSearchComponent,
    providers: [
      medienDataProvider
    ],
  },
];