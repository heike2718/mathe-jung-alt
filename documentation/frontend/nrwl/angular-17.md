# Neuerungen von Angular 17 einbauen

apps/mja-app/project.json

```
      "executor": "@angular-devkit/build-angular:browser-esbuild",

```

Funktioniert aber erst, wenn man auf deep-clone verzichtet! 

__hat funktioniert__ offenbar braucht man keine fremden libs mehr dafür :)

__doch nicht__: müssen das ausbauen. Gibt runtime errors


## select items component geht kaputt

wenn man die *ngFor ersetzt => TODO


lazy loading: Buttons zum navigieren zu anderem feature dürfen nur zu sehen sein, wenn das andere feature bereits geladen wurde.

oder ich bekomme heraus, wie ich alle feature eager lade