import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { SelectItemsComponent } from './select-items.component';


@NgModule({
    imports: [CommonModule, MatIconModule],
    declarations: [SelectItemsComponent],
    exports: [SelectItemsComponent],
})
export class SelectItemsComponentModule { }