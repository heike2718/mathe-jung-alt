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
                path: 'raetsel',
                loadChildren: () =>
                    import('@mathe-jung-alt-workspace/raetsel/feature-search').then((m) => m.RaetselFeatureSearchModule),

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