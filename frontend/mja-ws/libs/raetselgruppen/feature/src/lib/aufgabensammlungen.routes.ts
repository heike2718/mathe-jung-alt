import { Routes } from "@angular/router";
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { RaetselgruppenSearchComponent } from "./raetselgruppen-search/aufgabensammlungen-search.component";
import { raetselgruppenDataProvider } from '@mja-ws/raetselgruppen/api';
import { RaetselgruppenDetailsComponent } from "./raetselgruppen-details/aufgabensammlungen-details.component";
import { inject } from "@angular/core";
import { RaetselgruppeEditComponent } from "./raetselgruppe-edit/aufgabensammlungen-edit.component";

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
        path: 'edit',
        canActivate: [() => inject(AuthFacade).userIsAdmin$],
        component: RaetselgruppeEditComponent
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