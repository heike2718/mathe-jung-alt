import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { environment } from '../environments/environment';
import { HomeComponent } from './home/home.component';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
    { path: 'forbidden', component: NotAuthorizedComponent },
    {
        path: 'quellen',
        loadChildren: () =>
            import('@mja-workspace/quellen/feature-search').then((m) => m.QuellenFeatureSearchModule),

    },
    {
        path: 'raetsel',
        loadChildren: () =>
            import('@mja-workspace/raetsel/feature-search').then((m) => m.RaetselFeatureSearchModule),

    },
    {
        path: 'raetseleditor',
        loadChildren: () =>
            import('@mja-workspace/raetsel/feature-edit').then((m) => m.RaetselFeatureEditModule),

    },
    {
        path: 'raetselgruppen',
        loadChildren: () =>
            import('@mja-workspace/raetselgruppen/feature-search').then((m) => m.RaetselgruppenFeatureSearchModule),

    },
    {
        path: 'home',
        component: HomeComponent
    },
    {
        path: '',
        pathMatch: 'full',
        component: HomeComponent,
    },
    { path: '**', component: NotFoundComponent },
];

// { enableTracing: false, useHash: true, relativeLinkResolution: 'legacy' }
            
@NgModule({
    imports: [
        RouterModule.forRoot(
            routes,
            { enableTracing: false, useHash: true, relativeLinkResolution: 'legacy' }
            
        )
    ],
    exports: [
        RouterModule
    ]
})
export class AppRoutingModule { }