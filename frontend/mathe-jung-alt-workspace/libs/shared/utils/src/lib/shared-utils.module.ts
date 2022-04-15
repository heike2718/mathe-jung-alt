import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedUiMessagingModule } from '@mathe-jung-alt-workspace/shared/ui-components';
import { StoreModule } from '@ngrx/store';

@NgModule({
  imports: [
    CommonModule,
    StoreModule,
    SharedUiMessagingModule
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
