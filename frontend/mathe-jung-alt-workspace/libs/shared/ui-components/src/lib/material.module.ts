import {NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule }  from '@angular/material/sidenav';
import { MatButtonModule} from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';

const materialModules = [
  MatToolbarModule,
  MatSidenavModule,
  MatButtonModule,
  MatTableModule,
  MatPaginatorModule,
  MatSortModule
];


  @NgModule({
    imports: [
        CommonModule,
        ...materialModules
    ],
    exports: [
        CommonModule,
        ...materialModules
    ]
  })
export class MaterialModule {}