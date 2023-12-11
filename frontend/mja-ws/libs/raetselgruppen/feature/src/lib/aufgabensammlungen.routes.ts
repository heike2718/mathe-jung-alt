import { Routes } from "@angular/router";
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { AufgabensammlungenSearchComponent } from "./raetselgruppen-search/aufgabensammlungen-search.component";
import { aufgabensammlungenDataProvider } from '@mja-ws/raetselgruppen/api';
import { AufgabensammlungDetailsComponent } from "./raetselgruppen-details/aufgabensammlungen-details.component";
import { inject } from "@angular/core";
import { AufgabensammlungEditComponent } from "./raetselgruppe-edit/aufgabensammlungen-edit.component";

// siehe https://www.angulararchitects.io/en/aktuelles/modern-and-lightweight-angular-architectures-with-angulars-latest-innovations/

export const aufgabensammlungenRoutes: Routes = [
    {
        path: 'uebersicht',
        canActivate: [() => inject(AuthFacade).userIsAdmin$],
        component: AufgabensammlungenSearchComponent,
        providers: [
            aufgabensammlungenDataProvider
        ],
    },
    {
        path: 'details',
        canActivate: [() => inject(AuthFacade).userIsAdmin$],
        component: AufgabensammlungDetailsComponent
    }, 
    {
        path: 'edit',
        canActivate: [() => inject(AuthFacade).userIsAdmin$],
        component: AufgabensammlungEditComponent
    },
    {
        path: '',
        pathMatch: 'full',
        canActivate: [() => inject(AuthFacade).userIsAdmin$],
        component: AufgabensammlungenSearchComponent,
        providers: [
            aufgabensammlungenDataProvider
        ],
    }
];