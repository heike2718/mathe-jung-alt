# Aufgabenarchiv

Neue Anwendung: __mja-aufgabenarchiv-admin__

## POC LaTeX-Generierung auf dem Server

* LaTeX - full- Installation als container
* Experimente mit den von mir benötigten LaTeX- Befehlen
* ps2png als container verfügbar machen


## Übernahme der Aufgaben-Metadaten auf den Server

Mit einer neuen Web-Anwendung soll in Zukunft das Aufgabenarchiv gepflegt werden. Dies die Eclipse-RCP-Anwendung ablösen und von anderen mit genutzt werden können.

Aufgaben sollen mehrfach verwendet werden können. Dazu Lösung von der zu 1- Zuordnung zu einer Klassenstufe. Statt starre Attribute übergang zu Tags.

__Welche Tags sind erforderlich für die Migration des bisherigen Archivs?__

* Stufe:
   * Serien haben die Stufen Vorschule, 1/2, 3/4, ..., 9/13.
   * Minikänguruaufgaben bekommen die neuen Stufen IKID, EINS, ZWEI
   * Quizaufgaben bekommen die Stufen Grundschule, Sek 1, Sek 2
* Level: A, B, C (leicht, mittel, schwer)
* Zweck: momentan Serie und Minikänguru
* Aufgabenart: E(igenbau), N(achbau), Z(itat)
* Thema: sind mathematische Themen
* Jahreszeit

Neue Tags müssen einfach erfasst und zugeordnet werden können. Tags einer Kategorie können mehrfach vergeben werden.
Nach Tags muss gesucht werden können.

__Kommentarfunktion:__

Aufgaben müssen kommentiert werden können. Eine Aufgabe kann mehrere Kommentare bekommen

__Bewertungsfunktion:__

Aufgaben müssen bewertet werden können nach verschiedenen Kriterien

* Sprache / Verständlichkeit?
* Level angemessen? ja, zu leicht, zu schwer

__Quellen:__

Diese Funktion ist erst später erforderlich, wenn das gesamte Aufgabenarchiv migriert wird.
Verwendete Quellen müssen migriert werden. Neue Medien müssen erfasst werden können und als Quellen zugeordnet werden können.

Fragen:

* Wie wird in der Übergangszeit verfahren: wenn ich das Aufgabenarchiv bereits erweitern möchte, aber noch nicht migriert habe? * Ist eine automatische Migration erforderlich? Alternativ manuelle Migration mit Ausmisten.

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

__Nachteile Filesystem:__

* eigener Backup-Mechanismus für Files

__Vorteile Filesystem:__

