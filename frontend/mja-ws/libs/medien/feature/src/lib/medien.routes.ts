import { inject } from "@angular/core";
import { Routes } from "@angular/router";
import { AuthFacade } from '@mja-ws/core/api';
import { MedienSearchComponent } from "./medien-search/medien-search.component";
import { medienDataProvider } from "@mja-ws/medien/api";


export const medienRoutes: Routes = [
    {
        path: 'uebersicht',
        canActivate: [() => inject(AuthFacade).userIsAdmin$],
        component: MedienSearchComponent,
        providers: [
            medienDataProvider
        ]
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