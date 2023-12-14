# Struktur

Siehe [Taming Code Organization with Module Boundaries in Nx](https://dev.to/nx/taming-code-organization-with-module-boundaries-in-nx-1icl)

## Dependency Graphs

### mja-app mit direkten Abhängigkeiten

![mja-app mit direkten Abhängigkeiten](./mja-app-direct-dependencies.png "direkte Abhängigkeiten mja-app")

### mja-app mit den aktuellen domains scopes

![mja-app with scopes](./mja-app-with-domain-scopes.png "mja-app - scopes")


### domain raetsel mit Abhängigkeiten

![raetsel](./raetsel.png "raetsel")

### feature raetsel mit direkten Abhängigkeiten

![feature raetsel](./raetsel-feature.png "feature raetsel")


### domain aufgabensammlunen mit Abhängigkeiten

![aufgabensammlungen](./aufgabensammlungen.png "aufgabensammlungen")

## Typen von Libraries

### Haupttypen

- app - Eine Anwendung
- feature (UI-Components zu einem domain)
- api (Interfaces, die von feature verwendet werden)
- data (kapselt die Daten eines features - nur durch api zugänglich)
- model (fachliche domain-Objekte)
- utils (übergreifende Funktionen)
- shared (UI-Components, Layout, Auth, Messages)

### Untertypen

- domain:api
- domain-model
- core-api
- core-model
- 


