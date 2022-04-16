import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { RouterModule } from '@angular/router';
import { RaetselDomainModule } from '@mathe-jung-alt-workspace/raetsel/domain';
import { RaetselFeatureSearchModule } from '@mathe-jung-alt-workspace/raetsel/feature-search';
import { LoadingIndicatorComponentModule, MessageComponentModule } from "@mathe-jung-alt-workspace/shared/ui-messaging";
import { MaterialModule } from "@mathe-jung-alt-workspace/shared/ui-components";
import { DeskriptorenDomainModule } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { DeskriptorenSearchModule } from "@mathe-jung-alt-workspace/deskriptoren/feature-search";
import { HeaderComponentModule } from "./core/header/header.component";
import { SidemenuComponentModule } from "./core/sidemenu/sidemenu.component";
import { NotAuthorizedComponentModule } from "./core/not-authorized/not-authorized.component";


@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        MaterialModule,
        RouterModule,
        DeskriptorenDomainModule,
        DeskriptorenSearchModule,
        RaetselDomainModule,
        RaetselFeatureSearchModule,
        MessageComponentModule,
        LoadingIndicatorComponentModule,
        HeaderComponentModule,
        SidemenuComponentModule,
        NotAuthorizedComponentModule
    ],
    exports: [
        AppComponent
    ]
})
export class AppComponentModule { }