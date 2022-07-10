import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes = [
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
        path: '',
        pathMatch: 'full',
        component: HomeComponent,
    },
    { path: '**', component: NotFoundComponent },
];


@NgModule({
    imports: [
        RouterModule.forRoot(
            routes,
            // { enableTracing: false, useHash: true, relativeLinkResolution: 'legacy' }
            { enableTracing: false, useHash: false }
        )
    ],
    exports: [
        RouterModule
    ]
})
export class AppRoutingModule { }