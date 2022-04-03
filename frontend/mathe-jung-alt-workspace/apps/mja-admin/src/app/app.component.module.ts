import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { RouterModule } from '@angular/router';
import { RaetselDomainModule } from '@mathe-jung-alt-workspace/raetsel/domain';
import { RaetselFeatureSearchModule } from '@mathe-jung-alt-workspace/raetsel/feature-search';
import { MessageComponentModule } from "@mathe-jung-alt-workspace/shared/ui-messaging";
import { MaterialModule } from "./core/material.module";


@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        MaterialModule,
        RouterModule,
        RaetselDomainModule,
        RaetselFeatureSearchModule,
        MessageComponentModule
    ],
    exports: [
        AppComponent
    ]
})
export class AppComponentModule {}