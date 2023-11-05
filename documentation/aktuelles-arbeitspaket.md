# Woran ich gerade arbeite

Damit ich mich nach einigen Tagen Pause wieder erinnere, wo ich weitermachen muss.

## 01.11.2024

Arbeite an [Relativen Pfad und includegraphics beim Hochladen von Grafiken generieren](https://github.com/heike2718/mathe-jung-alt/issues/96)

Die raetsel-details.component ist soweit fertig.

In der grafik-details.component wurde die file-upload.component durch eine Kombination von select-file.component und file-info.component
ersetzt, analog zur raetsel-editor.

~~Zum Hochladen wird der grafik.state und die GrafikFacade verwendet.~~ 

~~grafik.state muss in embeddableImages.state integriert werden und alle Methode aus GrafikFacade müssen in EmbeddableImagesFacade umziehen.~~

~~Nach dem Hochladen der neuen Grafik wird das Vorschaubild in der grafik-details.component noch nicht ausgetauscht.~~


~~Also die GRAFIK_HOCHGELADEN - Action sollte das Laden der Vorschau auslösen. Da ist uns ein Event abhanden gekommen, das im file-upload noch~~
~~vorhanden war.~~


Zum Zeitpunkt des Ladens der Raetsel-Details, werden mittels regexp alle Pfade von eingebetteten Images extrahiert und die emeddableImageInfos werden 
erzeugt und in das RaetselDetailsDto gepackt.

Wenn jetzt eine nicht existierende Grafik hochgeladen wird, ändert sich der Pfad, weil neuerdings ein Name und ein neuer Pfad generiert werden.

Das ist aber eindeutig ein UseCase, der nur im raetsel-editor erledigt werden sollte. 

im raetsel-editor muss die grafik-info ausgewertet werden. Es müssen nicht existierende Pfade angezeigt werden und dann muss man sie 
hochladen können.

~~EmbeddableImagesInfo muss wissen, ob sie zur Frage oder zur Lösung gehört, damit man die embeddable-images-vorschau.component für den editor splitten~~ ~~kann.~~ Erledigt: textart ist Attribut auch beim Replace





__raetsel-details:__ ~~vorhandene embeddableImages anzeigen und austauschen. nicht vorhandene markieren und Verweis auf Editor~~

~~=> die upload-Komponente darf bei nicht vorhandenen Files nicht angeboten werden~~

__raetsel-editor:__ nicht vorhandene Files müssen angezeigt werden. Dann muss es möglich sein, die Datei hochzuladen. Das ist aber nur für eine
Übergangszeit nötig, bis alle aktuell hochgeladenen Files eine neue Wohnung und einen neuen Namen bekommen haben.

Sollte also markiert werden, dass das wieder raus muss.

Beim Speichern eines Rätsels müssen alle referenzierten Grafikdateien gelöscht werden, die jetzt nicht mehr referenziert werden.
Sonst gibt es viele Leichen.

Phase 1:

embeddable-image-vorschau.component im Editor muss für eine Übergangszeit auch Datei hochladen bekommen, wenn nicht vorhanden.
vorhandene Files müssen angezeigt und ausgetauscht werden können.

Phase 2: 

nicht vorhandene Files sollten nicht mehr hochgeladen werden können. Dafür gibt es die Aktion "neue Grafik hochladen"








Wenn das geschafft ist, kann file-upload ersatzlos entfernt werden.