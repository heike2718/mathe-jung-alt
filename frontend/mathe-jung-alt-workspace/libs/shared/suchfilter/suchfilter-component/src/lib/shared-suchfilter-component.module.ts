import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MaterialModule } from "@mathe-jung-alt-workspace/shared/ui-components";
import { AdminSuchfilterComponent } from "./admin/admin-suchfilter.component";
import { DeskriptorenFilterComponent } from "./deskriptoren-filter-component/deskriptoren-filter.component";


@NgModule({
    imports: [
        CommonModule,
        MaterialModule
    ],
    declarations: [
        AdminSuchfilterComponent,
        DeskriptorenFilterComponent
    ],
    exports: [
        AdminSuchfilterComponent,
        DeskriptorenFilterComponent
    ]
})
export class SharedSuchfilterComponentModule { }