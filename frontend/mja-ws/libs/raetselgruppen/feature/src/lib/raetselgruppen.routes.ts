import { Routes } from "@angular/router";
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { RaetselgruppenSearchComponent } from "./raetselgruppen-search/raetselgruppen-search.component";
import { raetselgruppenDataProvider } from '@mja-ws/raetselgruppen/api';
import { RaetselgruppenDetailsComponent } from "./raetselgruppen-details/raetselgruppen-details.component";
import { inject } from "@angular/core";

// siehe https://www.angulararchitects.io/en/aktuelles/modern-and-lightweight-angular-architectures-with-angulars-latest-innovations/

export const raetselgruppenRoutes: Routes = [
    {
        path: 'uebersicht',
        canActivate: [() => inject(AuthFacade).userIsAdmin$],
        component: RaetselgruppenSearchComponent,
        providers: [
            raetselgruppenDataProvider
        ],
    },
    {
        path: 'details',
        canActivate: [() => inject(AuthFacade).userIsAdmin$],
        component: RaetselgruppenDetailsComponent
    },
    {
        path: '',
        pathMatch: 'full',
        canActivate: [() => inject(AuthFacade).userIsAdmin$],
        component: RaetselgruppenSearchComponent,
        providers: [
            raetselgruppenDataProvider
        ],
    }
];