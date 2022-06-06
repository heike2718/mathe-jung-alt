import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { MaterialModule } from '@mathe-jung-alt-workspace/shared/ui-components';
import { LoadingInterceptor } from './loader/loading.interceptor';

@NgModule({
  imports: [CommonModule, MaterialModule],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      multi: true,
      useClass: LoadingInterceptor,
    },
  ],
})
export class SharedUiMessagingModule { }
