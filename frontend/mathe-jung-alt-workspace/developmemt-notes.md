# Notizen für Entwicklung

__material data tables eingebaut__

siehe [tutorial](https://www.positronx.io/angular-material-8-data-table-pagination-sorting-tutorial/)

im material.module.ts folgendes importiert:

```
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
```

Dann in app.module

```
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
...
bootstrap: [AppComponent],
schemas: [CUSTOM_ELEMENTS_SCHEMA]
```

__material-Modul nach ui-components verschoben__



__library ui-components erzeugt__

Das hier hat leider nicht geklappt: :(
```
npx nx generate @schematics/angular:library --name=ui-components --prefix=mja
```

daher ui-messages kopiert und die Teile angepasst

## 02.04.2022

__domain bilder generiert__

```
npx ng g @angular-architects/ddd:domain bilder

npx ng g @angular-architects/ddd:feature search --domain bilder --entity bild --noApp --ngrx
```


__domain quellen generiert__

```
npx ng g @angular-architects/ddd:domain quellen

npx ng g @angular-architects/ddd:feature search --domain quellen --entity quelle --noApp --ngrx
```


__domain medien generiert__

```
npx ng g @angular-architects/ddd:domain medien

npx ng g @angular-architects/ddd:feature search --domain medien --entity medien --noApp --ngrx
```


__domain raetsel generiert__

```
npx ng g @angular-architects/ddd:domain raetsel

npx ng g @angular-architects/ddd:feature search --domain raetsel --entity raetsel --noApp --ngrx
```
