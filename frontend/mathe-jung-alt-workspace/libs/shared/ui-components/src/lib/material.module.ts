import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatChipsModule } from '@angular/material/chips';

const materialModules = [
  MatToolbarModule,
  MatSidenavModule,
  MatButtonModule,
  MatTableModule,
  MatPaginatorModule,
  MatSortModule,
  MatChipsModule
];


@NgModule({
  imports: [
    CommonModule,
    ...materialModules
  ],
  exports: [
    CommonModule,
    ...materialModules,
  ]
})
export class MaterialModule { }