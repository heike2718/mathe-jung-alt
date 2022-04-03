import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';
import { RaetselDomainModule } from '@mathe-jung-alt-workspace/raetsel/domain';
import { RaetselFeatureSearchModule } from '@mathe-jung-alt-workspace/raetsel/feature-search';


@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        MatToolbarModule,
        RouterModule,
        RaetselDomainModule,
        RaetselFeatureSearchModule,    
    ],
    exports: [
        AppComponent
    ]
})
export class AppComponentModule {}