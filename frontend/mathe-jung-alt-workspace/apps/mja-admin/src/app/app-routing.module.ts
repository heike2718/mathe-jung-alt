import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { NotAuthorizedComponent } from "./core/not-authorized/not-authorized.component";
import { HomeComponent } from "./home/home.component";

const Routes = [
    { path: 'forbidden', component: NotAuthorizedComponent },
    {
        path: '',
        pathMatch: 'full',
        component: HomeComponent,
        children: [
            {
                path: 'dashboard',
                loadChildren: () =>
                    import('./dashboard/dashboard-component.module').then((m) => m.DashboardComponentModule),
            },
            {
                path: 'quellen',
                loadChildren: () =>
                    import('@mathe-jung-alt-workspace/quellen/feature-search').then((m) => m.QuellenFeatureSearchModule),

            },
            {
                path: 'raetsel',
                loadChildren: () =>
                    import('@mathe-jung-alt-workspace/raetsel/feature-search').then((m) => m.RaetselSearchModule),

            },
            {
                path: 'raetseleditor',
                loadChildren: () =>
                    import('@mathe-jung-alt-workspace/raetsel/feature-edit').then((m) => m.RaetselEditModule),

            },
            {
                path: '',
                pathMatch: 'full',
                component: HomeComponent,
            },
        ]
    },
    { path: '**', component: HomeComponent },
];

@NgModule({
    imports: [
        RouterModule.forRoot(Routes)
    ],
    exports: [
        RouterModule
    ]
})
export class AppRoutingModule { }