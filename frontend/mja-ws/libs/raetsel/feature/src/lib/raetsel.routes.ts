import { Routes } from "@angular/router";
import { RaetselSearchComponent } from "./raetsel-search/raetsel-search.component";
import { raetselDataProvider } from '@mja-ws/raetsel/data';


export const raetselRoutes: Routes = [
    {
      path: '',
    //   canActivate: [DataGuard],
      component: RaetselSearchComponent,
      providers: raetselDataProvider,     
    },
  ];
  