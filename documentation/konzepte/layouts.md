# Layouts für die generierten Rätsel und Rätselgruppen

Aktuell haben wir quiz-vorschau und raetsel, wobei beim PDF die Lösung direkt unter die Aufgabe gedruckt wird. 

Für Leher:innen sinnvoll wäre ein Kartei-Format, in dem Aufgabe und Lösung auf 2 verschiedene Seiten eines A5-Blattes gedruckt werden.

## Layout-Typen

+ Quizvorschau
  + alle Elemente der Rätselgruppe werden separiert nach Rätsel und Lösungen hintereinander weg gedruckt als PDF
  + erst kommen alle Fragen in der gegebenen Reihenfolge
  + dann kommen alle Lösungen in der passenden Reihenfolge

+ Knobelkartei
  + aus Zusammenstellungen von Rätseln soll sich eine Kartei drucken lassen, in der jede Frage und jede Lösung auf aufeinander folgenden separaten Seiten stehen. Dann kann man sie mit 2 Seiten je Blatt drucken, zu A5 zurechtschneiden und laminieren.

+ Rätsel als PDF
  + zum Ausdrucken

Streng genommen entspricht der PDF-Druck eines einzelnen Rätsels dem Druck einer Rätselgruppe, die nur ein Element enthält.

+ Aufgabenblätter
  + aus Zusammenstellungen von Rätseln sollen sich Aufgabenblätter drucken lassen
  + Schwierigkeit: möglichst keine Seitenunbrüche innerhalb einer Augabe? Oder sollte das egal sein?
  + Es werden 2 PDFs generiert: eins mit den Fragen, eins mit den Lösungen.
  + Es soll gewählt werden können, ob die Antwortvorschläge mit gedruckt werden sollen.

  
+ Rätsel als PNG (je eins für die Frage, eins für die Lösung)
  + zur Anzeige in der mja-admin-App
  + zur Anzeige in online-Quizes
  + zur Anzeige in der Minikänguru-Open-Data-App, in der die Statistik aufbereitet wird


## Was soll als Variablen wähbar sein?

+ Layout der Antwortvorschläge (wenn es keine gibt oder wenn keine gedruckt werden sollen, kann das ignoriert werden)
+ Font: wählbar sind
  + Fibel Nord
  + Fibel Süd
  + LaTeX-Standard

Mit der Wahl des Fonts wird gleichzeitig festgelegt, mit wieviel pt getexed wird: Die Fibel-Fonts bekommen 12pt, LaTeX-Standard bekommt 11pt.

## Welche Generatoren benötigen wir

+ Generator für Quizvorschau
  + Rätsel sollen sortiert sein.
  + Font muss nicht wählbar sein
  + Schriftgröße muss nicht wählbar sein

+ Generator für Rätsel-PNGs
  + Font muss wählbar sein
  + Schriftgröße muss wählbar sein (oder nehmen wir immer 12pt für diesen Zweck?)

+ Generator für Knobelkartei

+ Generator für Aufgabenblätter


## Aktionen für ADMINs und AUTORen

### Generiere PNGs für Rätsel

Diese Aktion ist nur im Rätseleditor verfügbar.

Falls es Antwortvorschläge gibt, muss das Layout gewählt werden. Falls es keine gibt, ist Layout NOPE.

Es kann der Font gewählt werden. Wenn keiner gewählt ist, wird der Standard-LaTeX-Font genommen.

Dann werden 2 PNGs generiert, eins für die Frage, eins für die Lösung. Beide werden im Payload zurückgegeben. Ob und wie die Client-Anwendung sie anzeigt, interessiert die API nicht.

### Generiere PDF für Rätsel

Das ist als Vorschau gedacht. Daher wird die Lösung ohne Seitenumbruch direkt hinter die Frage gedruckt.

Wenn es Antwortvorschläge gibt, muss gewählt werden, ob Multiple-Choice gedruckt werden soll. Bei "Ja" muss das Layout der Antwortvorschläge gewählt werden. Bei "Nein" dürfen Antwortvorschläge nicht gedruckt werden, auch wenn es welche gibt. Entspricht LayoutAntwortvorschlaege.NOPE.

Es wird mit LaTeX-Standardfont, 11pt gedruckt.

### Generiere Vorschau für Rätselgruppen

Diese Aktion ist nur im Feature Rätselgruppen verfügbar. Es soll eine schnelle Vorschau des Inhalts einer gewählten Rätselgruppe als PDF generiert werden, erst alle Fragen, dann Seitenumbruch, dann alle Lösungen.

Sortiert wird nach gegebener Nummer.

Wenn es ein Element mit 2 oder mehr Antwortvorschlägen gibt, muss gewählt werden, ob Multiple-Choice gedruckt werden soll. Bei "Ja" muss das Layout der Antwortvorschläge gewählt werden. Bei "Nein" dürfen Antwortvorschläge nicht gedruckt werden, auch wenn es welche gibt. Entspricht LayoutAntwortvorschlaege.NOPE.

Es kann der Font gewählt werden. Wenn keiner gewählt wurde wird Standard-LaTeX gedruckt.

Nur ADMIN und AUTOR kann diese Akion ausführen.

## Aktionen für authentifizierte Benutzer (Lehrer, Privatpersonen)

Diese Personen sollen eigene Rätselgruppen anlegen können. Die Rätselgruppen sollten noch einen Typ bekommen.

+ RESTRICTED
+ PUBLIC

PUBLIC Rätselgruppen soll jeder autentifizierte User anlegen und bearbeiten können. RESTRICTED Rätselgruppen sollen nur ADMINs und AUTORen anlegen und bearbeiten dürfen.

In der Webanwendung für die Allgemeinheit können nur PUBLIC Rätselgruppen angelegt werden. Man kann nur seine eigenen Rätselgruppen sehen, bearbeiten und löschen.

Zur Auswahl stehen nur freigegebene Rätsel.

### Generiere Knobelkartei

Ziel ist eine Rätselgruppe oder ein einzelnes Rätsel als PDF zu drucken. Die Uminterpretation eines einzelnen Rätsels zu einer Rätselgruppe geschieht auf dem Server.

Die Aktion ist im Rätseleditor verfügbar für Admins und Autoren. Status ist egal.

Bei einer Knobelkartei ist die Reihenfolge der Elemente egal.

Wenn es ein Element mit 2 oder mehr Antwortvorschlägen gibt, muss gewählt werden, ob Multiple-Choice gedruckt werden soll. Bei "Ja" muss das Layout der Antwortvorschläge gewählt werden. Bei "Nein" dürfen Antwortvorschläge nicht gedruckt werden, auch wenn es welche gibt. Entspricht LayoutAntwortvorschlaege.NOPE.

Es kann der Font gewählt werden. Wenn keiner gewählt wurde wird Standard-LaTeX gedruckt.

Dann werden die Rätsel wie folgt gedruckt:

+ Frage
+ Seitenumbruch
+ Lösung
+ Seitenumbruch
+ ...
+ Lösung

(Nach der Lösung des letzten Elements kein Seitenumbruch mehr)

### Generiere Aufgabenblätter

Ziel ist eine Rätselgruppe mit mindestens 2 Elementen. Ein Aufgabenblatt mit nur einem Element ist von einer Knobelkartei nicht unterscheidbar.

Die Reihenfolge der Elemente muss durch die Anwendenden festelgbar sein. In den Rätselgruppen geschieht das aktuell durch die Wahl einer Nummer. Es sollte aber auch möglich sein, die Rätsel in der gewünschten Reihenfolge ohne Nummer zu sorieren (drag and drop oder ^^^- Aktionen auf der Liste). In diesem Fall wird die Nummer beim Umsortieren aktualisiert, also erst einmal 001, 002, ... Der Umsortieralgorithmus ist herausfordernd.

Enthält die Rätselgruppe ein Multiple-Choice-Element, muss gewählt werden, ob als Multiple-Choice gedruckt werden soll. Bei "Ja" muss das Layout der Antwortvorschläge gewählt werden. Bei "Nein" dürfen Antwortvorschläge nicht gedruckt werden, auch wenn es welche gibt. Entspricht LayoutAntwortvorschlaege.NOPE.

Es kann der Font gewählt werden. Wenn keiner gewählt wurde wird Standard-LaTeX gedruckt.

Es soll möglich sein, Seitenumbrüche vorzugeben. Den Probedruck kann man ohne Seitenumbrüche erstellen. Dann kann man zwischen einzelnen Rätseln Seitenumbrüche definieren.

Dann werden die Rätsel wie folgt gedruckt:

+ Frage 1
+ Frage 2
+ ...
+ Seitenumbruch (also hintereinander Fragen bis zum Seitenumbruch)
+ Frage n
+ Seitenumbruch
+ Lösung 1
+ Lösung 2
+ ...
+ Lösung m

Die Lösungen werden hintereinander weg ohne Seitenumbruch gedruckt.

## Kategorisierung

Welche Merkmale zur Kategorisierung der Generierungsaufgabe gibt es?

+ Ausgabeformat (PDF, PNG, LaTeX)
+ Verwendungszweck (Aufgabenblatt, Aufgaben mit Lösungen, nur Aufgabe, nur Lösung)
+ Inhalte (Frage, Lösung)
+ Trenner aufgabe-loesung (kleiner Abstand, Seitenumbruch)

PDF und LaTeX sind keine getrennten Aufgabenformate im eigentlichen Sinne. LaTeX ist die Vorstufe für PDF. D.h. man kann eine Methode schreiben, die das LaTeX erzeugt und als String zurückgibt. Darauf kann dann eine Methode aufsetzen, die den String in eine LaTeX-Datei schreibt und deren Pfad zurückgibt. Diese wiederum kann dem LaTeX-Service zur Generierung des PDF übergeben werden.

Inhalt "nur Aufgabe" und "nur Lösung" sind nur für PNG erforderlich

Der Trenner ist für PNG uninteressant.

__Fazit:__

### PNG

Ausgabeformat = PNG.

2 mögliche Inhalte: Frage, Lösung. Kein Rahmen.

1. template-png-frage.txt
1. template-png-loesung.txt

### PDF

Ausgabeformat = PDF

3 mögliche Rahmen: Arbeitsblatt, Aufgaben mit Lösungen (für Aufgabenblätter), Kartei.

Inhalte zusammengefasst oder getrennt.

__Für die Rahmen:__

1. template-aufgabenblatt-pdf.tex
1. template-aufgabenblatt-mit-loesungen-pdf.tex
1. template-kartei-pdf.tex

__Für die Rätselgruppenelemente:__

1. tmplate-pdf-frage.txt
2. template-pdf-loesung.txt
3. template-pdf-frage-loesung.txt

Für eine Kartei ist Trenner aufgabe-loesung ein Seitenumbruch, für eine Vorschau ein kleiner Abstand. D.h. im Fall von PDF muss die Art des Trenners als Parameter mitgegeben werden.



