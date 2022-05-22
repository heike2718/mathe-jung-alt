# Aufgabenarchiv

Neue Anwendung: __mja-aufgabenarchiv-admin__

## Entitäten

* Aufgaben (schluessel, text, loesungstext, deskriptoren, quellen, titel, bemerkung, antwortvorchläge(text, richtigmarker), status)
* Medien (schluessel, art, name, url, deskriptoren)
* Quellen (schluessel, medium, ausgabe, jahrgang, seite, name, autor, art)
* Aufgabensammlungen (schluessel, name, deskriptoren, aufgabennitems[aufgabe, nummer, punktzahl], nachbearbeitetes LaTeX wegen Seitenumbrüchen etc.)
* Wettbewerbe (aufgabensammlungen, loesungszettel, jahr)
* Bilder (pfad, schluessel, eps, deskriptoren)
  
## Valueobjekte

* Deskriptoren sind Schlagworte, die aus mehreren Worten bestehen können

D.h. ein Deskriptor braucht eine Klammer: (Stufe x), (Klasse 2), (Minikänguru 2020), (Zahlentheorie), (MC), ...

Metasuche in Deskriptoren: alle Deskriptoren laden, dann type ahead- Suche in inputfield. Treffer kann ausgesucht werden. So können mehrere Deskriptoren gesammelt und an eine Suche übergeben werden.

Suchen mit "und" - also like permutiert.

Wäre es gut für einen Deskriptor zu wissen, wie viele Entitäten ihn haben?

## Als Admin

* Entitäten pflegen
* Entitäten suchen
    * nach Deskriptoren (mehrere, in beliebiger Reihenfolge mit und)
    * nach Teilstrings im namen
    * nach worten im text / lösungstext  - sollten Texte indiziert werden? Dann Füllwortmenge festlegen

* Aufgaben müssen einen Status haben, damit Vorbereitungen für Minikänguru möglich sind, ohne sie bereits zu veröffentlichen: also Status ERFASST (oder ENTWURF?), GENERIERT, FREIGEGEBEN, (weitere?)

* Um Minikänguru bauen zu können, wird mit Aufgabensammlungen gearbeitet, die als Grundgerüst genereirt werden können.

* Neben PDF und PNG für einzelne Aufgaben muss man auch LaTeX einer Aufgabensammlung generieren und herunterladen können, um es nachzubearbeiten.

## Übernahme der Aufgaben-Metadaten auf den Server

Mit einer neuen Web-Anwendung soll in Zukunft das Aufgabenarchiv gepflegt werden. Dies wird die Eclipse-RCP-Anwendung ablösen und von anderen mit genutzt werden können.

Aufgaben sollen mehrfach verwendet werden können. Dazu Lösung von der zu 1- Zuordnung zu einer Klassenstufe. Statt starre Attribute Übergang zu Deskriptoren.

__Welche Deskriptoren sind erforderlich für die Migration des bisherigen Archivs?__

* Stufe:
   * Serien haben die Stufen Vorschule, 1/2, 3/4, ..., 9/13.
   * Minikänguruaufgaben bekommen die neuen Stufen IKID, EINS, ZWEI
   * Quizaufgaben bekommen die Stufen Grundschule, Sek 1, Sek 2
* Level: A, B, C (leicht, mittel, schwer)
* Zweck: momentan Serie und Minikänguru
* Aufgabenart: E(igenbau), N(achbau), Z(itat)
* Thema: sind mathematische Themen
* Jahreszeit

Neue Deskriptoren müssen einfach erfasst und zugeordnet werden können. Deskriptoren einer Kategorie können mehrfach vergeben werden.
Nach Deskriptoren muss gesucht werden können.


__Kommentarfunktion:__

Aufgaben müssen kommentiert werden können. Eine Aufgabe kann mehrere Kommentare bekommen

__Bewertungsfunktion:__

Aufgaben müssen bewertet werden können nach verschiedenen Kriterien

* Sprache / Verständlichkeit?
* Level angemessen? ja, zu leicht, zu schwer

__Quellen:__

Diese Funktion ist erst später erforderlich, wenn das gesamte Aufgabenarchiv migriert wird.
Verwendete Quellen müssen migriert werden. Neue Medien müssen erfasst werden können und als Quellen zugeordnet werden können.

__Fragen:__

* Wie wird in der Übergangszeit verfahren: wenn ich das Aufgabenarchiv bereits erweitern möchte, aber noch nicht migriert habe? 

* Ist eine automatische Migration erforderlich? Alternativ manuelle Migration mit Ausmisten.

__UseCases:__

* Aufgabe erfassen und speichern

* PNG generieren und im Filesystem des Webservers zum Einbau in html als image ablegen

* Aufgaben zu Sammlungen zusammenstellen (kristalline Aufgabensammlungen - Minikänguru, Serien, Hefte und volatile Aufgabensammlungen, die sich Lerher:innen zusammenclicken können sollen) (Sollen Lehrer:innen auch kristalline Aufgabensammlungen speichern können?)

__Sourcen:__

* Der Aufgaben- und Lösungstext (LaTeX) muss übernommen werden.
* Es muss eine Generierungsfunktion auf dem Server geben, der eine Aufgabe auf einfache Weise in ein png umwandelt (LaTeX-Compiler + Konvertierungsskripte)

# Archiv der Minikänguru-Aufgaben

Ausgangspunkt sind die Images von C.T. Diese wandern auf den Server und müssen katalogisiert werden.

Name der Bilddatei -> LaTeX- Datei auf dem Server über SCHLUESSEL verbunden.

Alternative: Aufgabentext in die DB und für das Generieren temporäre Datei erzeugen, LaTeX-Compiler darüberlaufen lassen, ps-png-Konverter laufen lassen und temporäre Datei wieder löschen?

__Nachteile Datenbank:__

* dumps werden riesig.
  
* backup recovery aufwendig?

__Vorteile Datenbank:__

* keine I/O beim Anzeigen der Quelltexte
  
* alles zu einer Aufgabe in einziger Datei
  
* Volltextsuche möglich

* Texte können einfach versioniert werden

__Nachteile Filesystem:__

* eigener Backup-Mechanismus für Files

__Vorteile Filesystem:__

* fällt mir gerade keiner ein


__Verknüpfung mit Statistik:__

In der OpenDataApp: png der einzelnen Aufgaben laden, Lösungsstatistk der einzelnen Aufgaben laden und als Tortengrafik visualisieren
Für das PNG wird eine Semantik im Aufgabenarchriv benötigt: Name der Aufgabensammlung und Nummer der Aufgabe => UUID und damit Schlüssel der Aufgabe => URL des Images.

Aufgabensammlung wird als JSON geladen und von OpenDataApp verwendet, um die URL der einzelnen Images zu ermitteln.

(ist alles noch sehr schwammig)


## Aufgabensammlungen

Frage: wie sollen die Inhalte von Aufgabensammlungen persistiert sein?

Aufgabensammlungen sind Gruppen von Aufgaben mit einem Namen. Die Aufgaben haben dabei eine feste Reihenfolge. PDFs von Aufgabensammlungen sollen einfach neu generiert werden.

Eine Aufgabensammlung ist eine eigene Entität mit einem Namen und einer Liste von Items, wobei ein Item eine Aufgabe referenziert und eine alphanumerische Nummer und ggf. noch eine Sortiernummer hat.

Vorhandene Aufgabensammlungen sind 

* Serien
* Minikänguruwettbewerbe mit Stufe (also bis 2016 je eine Aufgabensammlung, von 2017 bis 2019 je zwei Aufgabensammlungen, ab 2020 je 3 Aufgabensammlungen)
* Hefte (aber da ist fraglich, ob wir die migrieren sollten)
* Kleine Quizes (zukünftig)

Die Persistierung von Aufgabensammlungen dient der Datensicherung

# Nächste Schritte

* Aufsetzen frontend-nrwl-workspace mit nx-cli und angularachitects/ddd-cli. (erledigt)

* docker-laxtex-client zum Konvertieren von LaTeX zu PDF und PNG. (erledigt)

* Geneatoren für LaTeX-File einer einzelnen Aufgabe (erledigt)

* Frontend: Editor für Aufgaben mit Metadaten, zunächst nur selbst als Quelle wegen Minikänguru



