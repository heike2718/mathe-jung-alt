import { NgModule } from "@angular/core";
import { AppComponent } from "./app.component";
import { RouterModule } from '@angular/router';
import { RaetselDomainModule } from '@mathe-jung-alt-workspace/raetsel/domain';
import { RaetselFeatureSearchModule } from '@mathe-jung-alt-workspace/raetsel/feature-search';
import { MessageComponentModule } from "@mathe-jung-alt-workspace/shared/ui-messaging";
import { MaterialModule } from "@mathe-jung-alt-workspace/shared/ui-components";
import { DeskriptorenDomainModule } from "@mathe-jung-alt-workspace/deskriptoren/domain";
import { DeskriptorenSearchModule } from "@mathe-jung-alt-workspace/deskriptoren/feature-deskriptoren-search";


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
        MessageComponentModule
    ],
    exports: [
        AppComponent
    ]
})
export class AppComponentModule {}