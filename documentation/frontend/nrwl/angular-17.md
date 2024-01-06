# Neuerungen von Angular 17 einbauen

apps/mja-app/project.json

```
      "executor": "@angular-devkit/build-angular:browser-esbuild",

```

Funktioniert aber erst, wenn man auf deep-clone verzichtet!


lazy loading: Buttons zum navigieren zu anderem feature dürfen nur zu sehen sein, wenn das andere feature bereits geladen wurde.

oder ich bekomme heraus, wie ich alle feature eager lade