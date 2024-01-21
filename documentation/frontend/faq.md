# Probleme, die auftraten und wie sie gelöst wurden

## Store feature war erst nach Aktivieren der jeweiligen route verfügbar

Wenn nicht alle Routen manuell einmal aufgerufen wurden, war die jeweilige Store-Section (feature) nicht verfügbar und die Navigation zwischen den feature modules funktionierte nicht, beispielsweise das Navigieren aus einer Aufgabensammlung zu den Rätsel-Details oder aus den Rätsel-Details zur verlinkten Aufgabensammlung oder aus Mediendetails zum verlinkten Rätsel.

Lange dachte ich, es müsse mit dem lazy loading der 3 Feature modules zu tun haben, aber außer das lazy loading komplett auszuschalten und alle Module eager zu laden, fiel mir nichts mehr ein, was ich machen konnte.

Am Ende war es dann absolut einfach:

_Lösung:_

Die jeweiligen Data-Providers müssen als Provider importiert werden:

app.config.ts

```
...
import { aufgabensammlungenDataProvider } from '@mja-ws/aufgabensammlungen/api';
import { raetselDataProvider } from '@mja-ws/raetsel/api';
import { medienDataProvider } from '@mja-ws/medien/api';

...

export const appConfig: ApplicationConfig = {
  providers: [
    ...authDataProvider,

...
   aufgabensammlungenDataProvider,
    raetselDataProvider,
    medienDataProvider,
...

```

