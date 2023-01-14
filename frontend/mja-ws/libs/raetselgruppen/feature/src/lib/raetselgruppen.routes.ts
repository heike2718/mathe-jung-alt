import { Routes } from "@angular/router";
import { AdminGuard } from '@mja-ws/shared/auth/api';
import { RaetselgruppenSearchComponent } from "./raetselgruppen-search/raetselgruppen-search.component";
import { raetselgruppenDataProvider } from '@mja-ws/raetselgruppen/api';

export const raetselgruppenRoutes: Routes = [
    {
        path: 'uebersicht',
        canActivate: [AdminGuard],
        component: RaetselgruppenSearchComponent,
        providers: [
            raetselgruppenDataProvider
        ],
    },
    {
        path: '',
        pathMatch: 'full',
        canActivate: [AdminGuard],
        component: RaetselgruppenSearchComponent,
        providers: [
            raetselgruppenDataProvider
        ],
    }
];