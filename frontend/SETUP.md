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




