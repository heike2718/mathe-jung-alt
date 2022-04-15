import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MaterialModule } from "../../../../ui-components/src";
import { LodingIndicatorComponent } from "./loding-indicator.component";


@NgModule({
    imports: [CommonModule, MaterialModule],
    declarations: [LodingIndicatorComponent],
    exports: [LodingIndicatorComponent],
  })
  export class LoadingIndicatorComponentModule { 

    constructor(){
        
    }
  }