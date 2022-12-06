# Steps to setup a new angular application with nrwl

## workspace generieren lassen

 ```
 npx create-nx-workspace@14.8.6
 ```

## weitere dependencies hinzufügen

(package.json)

(geht auch mit npm install --save):

```
"@angular/cdk": "14.2.7",
"@angular/material": "14.2.5",
"@angular/router": "14.2.11",
"@ngrx/effects": "14.3.2",
"@ngrx/eslint-plugin": "14.3.2",
"@ngrx/router-store": "14.3.2",
"@ngrx/store": "14.3.2",
"@ngrx/store-devtools": "14.3.2",    
"@ngx-formly/core": "5.12.7",
"@ngx-formly/material": "5.12.7",
"@nrwl/angular": "14.8.6",
"rxjs": "~7.5.0",
"tailwindcss": "^3.1.4",
"tslib": "^2.3.0",
"zone.js": "~0.11.4"
```

Eingetragen und anschließend npm install

## material style hinzufügen

apps/../src/styles.scss

## AppModule entfernen

* die AppComponent muss als standalone markiert werden
* app.module.ts löschen
* welcome-component löschen
* main.ts anpassen: startet AppComponent. Die Module werden über provider-Methoden importiert (oder wie immer man das nennt.)

## HomeComponent als Anker für die route /

```
npx nx generate @nrwl/angular:component home --project=mja-admin --path=/apps/mja-admin/src/app --selector=mja-admin-home --standalone --no-interactive
```

wird in die imports in app.component.ts eingetragen


## routing initialisiert

Datei: app.routes.ts

exportiert die Konstante appRoutes mit zunächst einer HomeComponent für den root-path

wird in main.ts mittels provideRouter(appRoutes) provided


## layout stylen

* Verzeichnis navigation angelegt
* header generiert:

```
npx nx generate @nrwl/angular:component header --project=mja-admin --path=/apps/mja-admin/src/app/navigation --selector=mja-admin-header --standalone --no-interactive
```

* sidenav generiert

```
npx nx generate @nrwl/angular:component sidenav --project=mja-admin --path=/apps/mja-admin/src/app/navigation --selector=mja-admin-sidenav --standalone --no-interactive
```

beide Komponenten in app.component.ts importieren (imports: [...])

header und sidenav werden in layout projeziert.

## Responiveness

LayoutComponent bekommt einen BreakpointObserver injected und subscribed sich in ngInit auf Breakpoints.Handset.

HeaderComponent bekommt ebenfalls den BreakpointObserver injected und eine get isHandset()- Methode entscheidet dann darüber, ob der Name angezeigt wird.

HeaderComponent und SidenavComponent emitten ein sidenavToggle-Event, das im app.component.html ausgewertet wird.

## Linting Rules für dependency restrictions

Die erlaubten dependencies werden im globalen ./.eslintrc.json definiert und werten dabei tags aus, die den libraries oder apps in project.json gegeben werden.

## Shared Libraries

unter libs: Verzeichnis shared angelegt

### Config


```
npx nx generate @nrwl/js:library config --directory=shared --tags='domain:shared, type:shared:config' --buildable --no-interactive --dry-run
```

In einer configuration.ts wird eine Klasse mit public attribute baseUrl angelegt. Später können dort weitere Konfigurationen hinzukommen.

index.ts wird angepasst: Komponente raus, Configuration rein.

__Einbinden von Config:__

in mja-admin/.../main.ts als provider wie folgt einbinden:

```
   {
      provide: Configuration,
      useFactory: () => new Configuration(environment.baseUrl),
    },
```

Das hat den Vorteil, dass man in main.ts, wo man Zugriff auf environment hat, appspezifische Konfigurationen reinbasteln kann.

### auth


```
npx nx generate @nrwl/js:library api --directory=shared/auth --tags='domain:shared, type:shared:auth:api' --buildable --no-interactive --dry-run

npx nx generate @nrwl/js:library data --directory=shared/auth --tags='domain:shared, type:type:shared:auth:data' --buildable --no-interactive --dry-run

npx nx generate @nrwl/js:library model --directory=shared/auth --tags='domain:auth:model, type:model' --buildable --no-interactive --dry-run
```

Zwischendurch musste eine lib messaging angelegt werden, weil das Message-Interface erforderlich ist.

auth.api stellt eine Facade mit 3 Methoden und 3 Observables zur Verfügung, die alles abdecken, was eine Anwendung braucht. Die interne Implementierung erfolgt in auth.data.

### messaging

```
npx nx generate @nrwl/js:library api --directory=shared/messaging --tags='domain:shared, type:shared:messaging:api' --buildable --no-interactive --dry-run

npx nx generate @nrwl/js:library ui --directory=shared/messaging --tags='domain:shared:messaging:ui, type:ui' --buildable --no-interactive --dry-run

```

und .eslintrc.json um dependency-definitionen erweitert

### util

Enthält Helferfunktionen, sie keinerlei dependencies haben

```
npx nx generate @nrwl/js:library util --directory=shared --tags='domain:shared, type:shared:util' --buildable --no-interactive --dry-run
```

### ngrx-utils

```
npx nx generate @nrwl/js:library ngrx-utils --directory=shared --tags='domain:shared, type:shared:ngrx-utils' --buildable --no-interactive --dry-run
```

### http

hier residieren low level http utils wie Interceptors, die nahezu keine Abhängikeiten haben.

```
npx nx generate @nrwl/js:library http --directory=shared --tags='domain:shared, type:shared:http' --buildable --no-interactive --dry-run
```

## Deskriptoren

```
npx nx generate @nrwl/js:library model --directory=deskriptoren --tags='domain:deskriptoren, type:model' --buildable --no-interactive --dry-run
```


## FAQ

__Compilefehler Cannot parse tsconfig.base.json: PropertyNameExpected in JSON at position 891__

Ursache ist ein Komma nach dem letzeten Item in der path-Liste

__Ist das generierte jest.config.ts korrekt?__

Die generierten ibraries enthalten aktuell nicht die korrekte Implementierung in jest.config.ts. Hier muss transform wie folgt ersetzt weren:

`''
  transform: {
    '^.+\\.(ts|mjs|js|html)$': 'jest-preset-angular',
  },
  transformIgnorePatterns: ['node_modules/(?!.*\\.mjs$)'],
  snapshotSerializers: [
    'jest-preset-angular/build/serializers/no-ng-attributes',
    'jest-preset-angular/build/serializers/ng-snapshot',
    'jest-preset-angular/build/serializers/html-comment',
  ]
```

__Fehler beim Ausführen von jest-Tests__

In den .spec-Dateien den angular compiler zu importieren, hat geholfen:

```
import '@angular/compiler';
```

__Fehlermeldung preset: '../../../../jest.preset.js' nicht gefunden oder so__

Dann stimmt die Anzahl der ../ in der entprechenden jest.config.ts nicht.
 
