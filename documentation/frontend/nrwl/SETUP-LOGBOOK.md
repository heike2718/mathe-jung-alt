# Steps to setup a new angular application with nrwl

## workspace generieren lassen

 ```
 npx create-nx-workspace@14.8.6
 ```

Interaktiv: 

* monorepo
* angular
* name
* app-name
* standalone components yes/no? => yes
* add routing => yes
* default stylesheet format => scss
 
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

Testen der linting rules:

```
npx nx run-many --target=lint --skip-nx-cache
```

Testen des graphs:

```
npx nx graph
```

## Shared Libraries

unter libs: Verzeichnis shared angelegt

### Config


```
npx nx generate @nx/angular:lib config --directory=shared --tags='domain:shared, type:shared:config' --buildable --no-interactive --dry-run
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
npx nx generate @nx/angular:lib api --directory=shared/auth --tags='domain:shared, type:shared:auth:api' --buildable --no-interactive --dry-run

npx nx generate @nx/angular:lib data --directory=shared/auth --tags='domain:shared, type:type:shared:auth:data' --buildable --no-interactive --dry-run

npx nx generate @nx/angular:lib model --directory=shared/auth --tags='domain:auth:model, type:model' --buildable --no-interactive --dry-run
```

Zwischendurch musste eine lib messaging angelegt werden, weil das Message-Interface erforderlich ist.

auth.api stellt eine Facade mit 3 Methoden und 3 Observables zur Verfügung, die alles abdecken, was eine Anwendung braucht. Die interne Implementierung erfolgt in auth.data.

### messaging

```
npx nx generate @nx/angular:lib api --directory=shared/messaging --tags='domain:shared, type:shared:messaging:api' --buildable --no-interactive --dry-run

npx nx generate @nx/angular:lib ui --directory=shared/messaging --tags='domain:shared:messaging:ui, type:ui' --buildable --no-interactive --dry-run

```

und .eslintrc.json um dependency-definitionen erweitert

### loading indicator

```
npx nx generate @nrwl/angular:component loading-indicator --project=shared-messaging-ui --selector=mja-loader --standalone --no-interactive --dry-run
```

### util

Enthält Helferfunktionen, sie keinerlei dependencies haben

```
npx nx generate @nx/angular:lib util --directory=shared --tags='domain:shared, type:shared:util' --buildable --no-interactive --dry-run
```

### ngrx-utils

```
npx nx generate @nx/angular:lib ngrx-utils --directory=shared --tags='domain:shared, type:shared:ngrx-utils' --buildable --no-interactive --dry-run
```

### http

hier residieren low level http utils wie Interceptors, die nahezu keine Abhängikeiten haben.

```
npx nx generate @nx/angular:lib http --directory=shared --tags='domain:shared, type:shared:http' --buildable --no-interactive --dry-run
```

### layout

```
npx nx generate @nx/angular:lib layout --directory=shared --tags='domain:shared, type:shared:ui, type:ui' --buildable --no-interactive --dry-run
```


## core

hier residieren die Model-Interfaces, Store, api, die in vielen Komponenten benötigt werden

```
npx nx generate @nx/angular:lib api --directory=core --tags='domain:core, type:core:api' --no-interactive --dry-run
npx nx generate @nx/angular:lib data --directory=core --tags='domain:core, type:core:data' --no-interactive --dry-run
npx nx generate @nx/angular:lib model --directory=core --tags='domain:core, type:core:model' --no-interactive --dry-run
```

## includegraphics

Übernimmt das Hochladen von eps, die mittels \includegraphics in die Texte eingebettet werden können.

Unterschieden wird zwischen Anlegen (also Hochladen einer neuen eps) und Ändern (also Austauschen einer vorhandenen)

```
npx nx generate @nx/angular:library --name=embeddable-images-api --buildable=true --directory=libs/shared/embeddable-images/api --importPath=@mja-ws/libs/shared/embeddable-image/api --projectNameAndRootFormat=as-provided --skipModule=true --style=scss --tags="domain:embeddable-images", "type:api" --no-interactive --dry-run

npx nx generate @nx/angular:library --name=embeddable-images-data --buildable=true --directory=libs/shared/embeddable-images/data --importPath=@mja-ws/libs/shared/embeddable-images/data --projectNameAndRootFormat=as-provided --skipModule=true --style=scss --tags="domain:embeddable-images, type:data" --no-interactive --dry-run

npx nx generate @nx/angular:library --name=embeddable-images-model --buildable=true --directory=libs/shared/embeddable-images/model --importPath=@mja-ws/libs/shared/embeddable-images/model --projectNameAndRootFormat=as-provided --skipModule=true --style=scss --tags="domain:embeddable-images, type:model" --no-interactive
```

__lint:__

```
{
                "sourceTag": "domain:raetsel:feature",
                "onlyDependsOnLibsWithTags": [
                  "domain:raetsel:model",
                  "domain:raetsel:api",
                  "domain:grafik:api",
                  "domain:grafik:model",
                  "type:api",
                  "type:model"
                ]
              },
...

{
                "sourceTag": "domain:embeddable-images:model",
                "onlyDependsOnLibsWithTags": ["type:model"]
              },
              {
                "sourceTag": "domain:embeddable-images:data",
                "onlyDependsOnLibsWithTags": [
                  "domain:embeddable-images:model",
                  "type:shared:messaging:api"
                ]
              },
              {
                "sourceTag": "domain:embeddable-images:api",
                "onlyDependsOnLibsWithTags": [
                  "domain:embeddable-images:model",
                  "domain:embeddable-images:data"
                ]
              },
...              
```


## Deskriptoren

```
npx nx generate @nx/angular:lib model --directory=deskriptoren --tags='domain:deskriptoren, type:model' --buildable --no-interactive --dry-run
```

## localStorage synchronisieren mittels ngrx local storage

```
npx nx generate @nx/angular:lib local-storage-data --tags='domain:core' --buildable --no-interactive --dry-run
```

## File upload

ist eine library, die für das Hochladen von Files zuständig ist.

```
npx nx generate @nx/angular:lib model --directory=shared/upload --tags='domain:upload, type:model' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib data --directory=shared/upload --tags='domain:upload, type:data' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib api --directory=shared/upload --tags='domain:upload, type:api' --buildable --no-interactive --dry-run
```


## Grafik

ist eine library, die Grafiken für Rätsel zur Verfügung stellt.
Es ist sinnvoll, dafür ein eigenes Store-Feature zu haben, weil anderenfalls das Raetsel-Feature zu aufgebläht ist. 

Store/raetsel kann ohne Store/grafik auskommen. Und die meiste Zeit benötigt man libs/grafik nicht für die Raetsel.

```
npx nx generate @nx/angular:lib model --directory=grafik --tags='domain:grafik, type:model' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib data --directory=grafik --tags='domain:grafik, type:data' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib api --directory=grafik --tags='domain:grafik, type:api' --buildable --no-interactive --dry-run
```

## Rätsel

```
npx nx generate @nx/angular:lib model --directory=raetsel --tags='domain:raetsel:model, type:model' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib data --directory=raetsel --tags='domain:raetsel:data, type:data' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib api --directory=raetsel --tags='domain:raetsel:api, type:api' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib feature --directory=raetsel --tags='domain:raetsel:feature, type:feature' --buildable --no-interactive --dry-run
```

### RaetselSearchComponent

```
npx nx generate @nrwl/angular:component raetsel-search --project=raetsel-feature --path=/libs/raetsel/feature/src/lib --selector=mja-raetsel-search --standalone --no-interactive --dry-run
```


### RaetselDetailsComponent

```
npx nx generate @nrwl/angular:component raetsel-details --project=raetsel-feature --path=/libs/raetsel/feature/src/lib --selector=mja-raetsel-details --standalone --no-interactive --dry-run
```

### Component antwortvorschlag

```
npx nx generate @nrwl/angular:component antwortvorschlag --project=raetsel-feature --path=/libs/raetsel/feature/src/lib --selector=mja-antwortvorschlag --standalone --no-interactive --dry-run
```

### Component grafik-details

Das ist die Komponente, mit der Grafiken für die Rätsel hochgeladen werden können.


```
npx nx generate @nrwl/angular:component grafik-details --project=raetsel-feature --path=/libs/raetsel/feature/src/lib --selector=mja-grafik --standalone --no-interactive --dry-run
```

### RaetselEditorComponent

```
npx nx generate @nrwl/angular:component raetsel-editor --project=raetsel-feature --path=/libs/raetsel/feature/src/lib --selector=mja-raetsel-editor --standalone --no-interactive --dry-run
```

### raetsel-suchfilter-public


```
npx nx generate @nrwl/angular:component raetsel-suchfilter-public --project=raetsel-feature --path=/libs/raetsel/feature/src/lib --selector=mja-raetsel-suchfilter-pub --standalone --no-interactive --dry-run
```

### raetsel-suchfilter-admin


```
npx nx generate @nrwl/angular:component raetsel-suchfilter-admin --project=raetsel-feature --path=/libs/raetsel/feature/src/lib --selector=mja-raetsel-suchfilter-admin --standalone --no-interactive --dry-run
```

## Raetselgruppen

```
npx nx generate @nx/angular:lib model --directory=raetselgruppen --tags='domain:raetselgruppen:model, type:model' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib data --directory=raetselgruppen --tags='domain:raetselgruppen:data, type:data' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib api --directory=raetselgruppen --tags='domain:raetselgruppen:api, type:api' --buildable --no-interactive --dry-run
npx nx generate @nx/angular:lib feature --directory=raetselgruppen --tags='domain:raetselgruppen:feature, type:feature' --buildable --no-interactive --dry-run
```

### RaetselgruppenSearchComponent

```
npx nx generate @nrwl/angular:component raetselgruppen-search --project=raetselgruppen-feature --path=/libs/raetselgruppen/feature/src/lib --selector=mja-raetselgruppen-search --standalone --no-interactive --dry-run
```


### RaetselgruppenDetailsComponent

```
npx nx generate @nrwl/angular:component raetselgruppen-details --project=raetselgruppen-feature --path=/libs/raetselgruppen/feature/src/lib --selector=mja-raetselgruppen-details --standalone --no-interactive --dry-run
```

### RaetselgruppenelementeComponent

```
npx nx generate @nrwl/angular:component raetselgruppenelemente --project=raetselgruppen-feature --path=/libs/raetselgruppen/feature/src/lib --selector=mja-raetselgruppenelemente --standalone --no-interactive --dry-run
```

### RaetselgruppenelementDialogComponent

```
npx nx generate @nrwl/angular:component raetselgruppenelement-dialog --project=raetselgruppen-feature --path=/libs/raetselgruppen/feature/src/lib --selector=mja-raetselgruppenelement --standalone --no-interactive --dry-run
```

### RaetselgruppeEditComponent

```
npx nx generate @nrwl/angular:component raetselgruppe-edit --project=raetselgruppen-feature --path=/libs/raetselgruppen/feature/src/lib --selector=mja-raetselgruppe-edit --standalone --no-interactive --dry-run
```


## Shared Componensts library

```
npx nx generate @nx/angular:lib components --directory=shared --tags='domain:shared, type:shared:ui' --buildable --no-interactive --dry-run
```

Linting-Rules erweitert. Man kommt mit der Grafik jetzt leider nicht mehr hinterher

### Component frage-loesung-images


```
npx nx generate @nrwl/angular:component frage-loesung-images --project=shared-components --selector=mja-frage-loesung-images --standalone --no-interactive --dry-run
```

### Component file-upload

```
npx nx generate @nrwl/angular:component file-upload --project=shared-components --selector=mja-file-upload --standalone --no-interactive --dry-run
```

### Component select-printparameters-dialog

```
npx nx generate @nrwl/angular:component select-printparameters-dialog --project=shared-components --selector=mja-select-printparameters-dialog --standalone --no-interactive --dry-run
```

### Component select-items

```
npx nx generate @nrwl/angular:component select-items --project=shared-components --selector=mja-select-items --standalone --no-interactive --dry-run
```

### JaNeinDialog

```
npx nx generate @nrwl/angular:component ja-nein-dialog --project=shared-components --selector=mja-ja-nein --standalone --no-interactive --dry-run
```

### Medien

```
npx nx g @nx/js:lib model --directory=libs/medien --tags='type:model' --buildable --no-interactive --dry-run
npx nx g @nx/js:lib data --directory=libs/medien --tags='type:data' --buildable --no-interactive --dry-run
npx nx g @nx/js:lib api --directory=libs/medien --tags='type:api' --buildable --no-interactive --dry-run
npx nx g @nx/js:lib feature --directory=libs/medien --tags='type:feature' --buildable --no-interactive --dry-run
```

__Medien search component__

```
npx nx generate @nrwl/angular:component medien-search --project=medien-feature --path=/libs/medien/feature/src/lib --selector=mja-medien-search -style=scss --standalone --no-interactive --dry-run
```

__Medium details component__

```
npx nx generate @nrwl/angular:component medium-details --project=medien-feature --path=/libs/medien/feature/src/lib --selector=mja-medium-details --style=scss --standalone --no-interactive --dry-run
```



__Medium edit component__

```
npx nx generate @nrwl/angular:component medium-edit --project=medien-feature --path=/libs/medien/feature/src/lib --selector=mja-medium-edit --style=scss --standalone --no-interactive --dry-run
```

__Quelle component__

```
npx nx generate @nrwl/angular:component quelle --project=raetsel-feature --path=/libs/raetsel/feature/src/lib --selector=mja-quelle --style=scss --standalone --no-interactive --dry-run
```

__linked raetsel component__

```
npx nx generate @nrwl/angular:component linked-raetsel --project=medien-feature --path=/libs/medien/feature/src/lib --selector=mja-linked-raetsel --style=scss --standalone --no-interactive --dry-run
```



## FAQ

__refactor-> move in nrwl__

Beispiel: module bookings soll sub-module von customers werden:

```
npx nx g mv bookings --project-name bookings --destination customers/bookings
```

__Sessionid-Cookie wird nicht in den Request gesetzt__

Man muss den httpClient-Call mit {withCredentials: true} konfigurieren (wird bei mir im AddBaseUrlInterceptor gemacht) __und__ das Session-Cookie muss mit JSESSIONID beginnen.
Habe das Session-Cookie jetzt JSESSIONID_MJA_ADMIN genannt.

__Laufzeitfehler aus auth.effects__

Ich hatte statt der noopAction eine function zurückgegeben, die die noopAction generieren würde. Lösung vorerst: 

kein return in actionCreator sondern mit {dispatch: false} beenden


__Compilefehler Cannot parse tsconfig.base.json: PropertyNameExpected in JSON at position 891__

Ursache ist ein Komma nach dem letzeten Item in der path-Liste

__Ist das generierte jest.config.ts korrekt?__

Die generierten ibraries enthalten aktuell nicht die korrekte Implementierung in jest.config.ts. Hier muss transform wie folgt ersetzt weren:

```
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

__Reducer createFeature({...}) wirft compilation error TS2345: Argument of type is not assignable to parameter of type 'FeatureConfig & "optional properties are not allowed in the feature state"'.__

Nach stundenlanger Suche stellte sich heraus, dass die Syntax 

```
export interface RaetselState {
    ...
    readonly selectedId?: string;
    ...
};
```

nicht nicht mehr supported wird. Stattdessen muss man

```
export interface RaetselState {
    ...
    readonly selectedId: string | undefined;
    ...
};
```

verwenden. Menno, menno, menno.

__metareducer zum Synchronisieren des localStorage und zum Leeren des stores beim logout__


werden in main.ts so eingebunden:

```
...
const localStorageMetaReducer = localStorageReducer('auth', 'coreQuelle', 'coreDeskriptoren'); // <-- synchronisiert diese Slices des Store mit localStorage wegen F5.
const clearStoreMetaReducer = loggedOutMetaReducer;

const allMetaReducers = environment.production ? [localStorageMetaReducer] : [localStorageMetaReducer, clearStoreMetaReducer];

...
    /** das muss so gemacht werden, weil ohne den Parameter {} nichts da ist, wohinein man den state hängen könnte */
    provideStore(
      {},
      {
        metaReducers: allMetaReducers
      }
    ),
    provideEffects([LocalStorageEffects]),
    provideStoreDevtools(),

...
...
```

