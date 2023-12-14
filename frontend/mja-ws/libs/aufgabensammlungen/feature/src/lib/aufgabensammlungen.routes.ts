import { Routes } from "@angular/router";
import { AuthFacade } from '@mja-ws/core/api';
import { AufgabensammlungenSearchComponent } from "./aufgabensammlungen-search/aufgabensammlungen-search.component";
import { aufgabensammlungenDataProvider } from '@mja-ws/aufgabensammlungen/api';
import { AufgabensammlungDetailsComponent } from "./aufgabensammlung-details/aufgabensammlungen-details.component";
import { inject } from "@angular/core";
import { AufgabensammlungEditComponent } from "./aufgabensammlung-edit/aufgabensammlung-edit.component";

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