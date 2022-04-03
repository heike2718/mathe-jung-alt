import {NgModule} from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule }  from '@angular/material/sidenav';
import { MatButtonModule} from '@angular/material/button';
import { CommonModule } from '@angular/common';


  @NgModule({
    imports: [
        CommonModule,
        MatToolbarModule,
        MatSidenavModule,
        MatButtonModule
    ],
    exports: [
        CommonModule,
        MatToolbarModule,
        MatSidenavModule,
        MatButtonModule
    ]
  })
export class MaterialModule {}