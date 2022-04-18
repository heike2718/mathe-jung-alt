import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { StoreModule } from '@ngrx/store';
import * as fromSuchfilter from './+state/suchfilter.reducer';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    StoreModule.forFeature(fromSuchfilter.SUCHFILTER_FEATURE_KEY, fromSuchfilter.reducer)
  ],
})
export class SharedSuchfilterDomainModule { }
