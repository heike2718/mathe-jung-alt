import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { EffectsModule } from '@ngrx/effects';

import { AppComponent } from './app.component';
import { StoreModule } from '@ngrx/store';
import { reducers, metaReducers } from './+state';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { environment } from '../environments/environment';
import { StichworteDomainModule } from '@mathe-jung-alt-workspace/stichworte/domain';
import { StichworteFeatureStichwortsucheModule } from '@mathe-jung-alt-workspace/stichworte/feature-stichwortsuche';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    StichworteDomainModule,    
    EffectsModule.forRoot([]),
    StichworteFeatureStichwortsucheModule,
    StoreModule.forRoot(reducers, { metaReducers }), !environment.production ? StoreDevtoolsModule.instrument() : []],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
