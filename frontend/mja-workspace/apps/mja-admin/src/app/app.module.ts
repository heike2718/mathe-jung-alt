import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { SharedUiComponentsModule } from '@mja-workspace/shared/ui-components';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    SharedUiComponentsModule
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
