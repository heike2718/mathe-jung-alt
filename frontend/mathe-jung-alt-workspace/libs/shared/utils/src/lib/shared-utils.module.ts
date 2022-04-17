import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';

@NgModule({
  imports: [
    CommonModule,
    StoreModule,
  ],
  providers: [
    // {
    //   provide: HTTP_INTERCEPTORS,
    //   multi: true,
    //   useClass: LoadingInterceptor,
    // },
  ],
})
export class SharedUtilsModule {}
