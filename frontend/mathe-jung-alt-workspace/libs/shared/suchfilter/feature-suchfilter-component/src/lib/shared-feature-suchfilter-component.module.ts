import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MaterialModule } from "@mathe-jung-alt-workspace/shared/ui-components";
import { AdminSuchfilterComponent } from "./admin/admin-suchfilter.component";
import { DeskriptorenSearchComponent } from "./feature-search/deskriptoren-search.component";


@NgModule({
    imports: [
        CommonModule,
        MaterialModule
    ],
    declarations: [
        AdminSuchfilterComponent,
        DeskriptorenSearchComponent
    ],
    exports: [
        AdminSuchfilterComponent,
        DeskriptorenSearchComponent
    ]
})
export class SharedFeatureSuchfilterComponentModule { }