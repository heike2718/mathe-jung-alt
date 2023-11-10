# Woran ich gerade arbeite

Damit ich mich nach einigen Tagen Pause wieder erinnere, wo ich weitermachen muss.

## 01.11.2024

Arbeite an [Relativen Pfad und includegraphics beim Hochladen von Grafiken generieren](https://github.com/heike2718/mathe-jung-alt/issues/96)

Die raetsel-details.component ist soweit fertig.

~~In der grafik-details.component wurde die file-upload.component durch eine Kombination von select-file.component und file-info.component~~
~~ersetzt, analog zur raetsel-editor.~~

~~Zum Hochladen wird der grafik.state und die GrafikFacade verwendet.~~ 

~~grafik.state muss in embeddableImages.state integriert werden und alle Methode aus GrafikFacade müssen in EmbeddableImagesFacade umziehen.~~

~~Nach dem Hochladen der neuen Grafik wird das Vorschaubild in der grafik-details.component noch nicht ausgetauscht.~~


~~Also die GRAFIK_HOCHGELADEN - Action sollte das Laden der Vorschau auslösen. Da ist uns ein Event abhanden gekommen, das im file-upload noch~~
~~vorhanden war.~~

~~Zum Zeitpunkt des Ladens der Raetsel-Details, werden mittels regexp alle Pfade von eingebetteten Images extrahiert und die emeddableImageInfos werden~~
~~erzeugt und in das RaetselDetailsDto gepackt.~~

~~EmbeddableImagesInfo muss wissen, ob sie zur Frage oder zur Lösung gehört, damit man die embeddable-images-vorschau.component für den editor splitten~~ ~~kann.~~ ~~Erledigt: textart ist Attribut auch beim Replace~~


__raetsel-details:__ ~~vorhandene embeddableImages anzeigen und austauschen. nicht vorhandene markieren und Verweis auf Editor~~

~~=> die upload-Komponente darf bei nicht vorhandenen Files nicht angeboten werden~~

__raetsel-editor:__ 

~~Grafiken werden immer als neu. Das Ersetzen ist in der Detailansicht möglich.~~

~~Listen der eingebundenen Grafiken sollen getrennt nach Frage und Lösung angezeigt werden~~
~~Klick auf das Icon öffnet einen Dialog~~

~~Beim Speichern eines Rätsels müssen alle referenzierten Grafikdateien gelöscht werden, die jetzt nicht mehr referenziert werden.~~

~~Neuer Bug:~~
~~selectedVorschau oder EmbeddableImageResponseDto muss gelöscht werden, wenn aus den Details wegnavigiert wird.~~
~~Bug: in Details Datei neu hochladen, dann zurück zu Rätsel, dann zum Editor: der Frage-Text enthält am Ende \\includegraphics des letzten Results.~~

~~raetsel-details.component: für EmbeddableImagesContext sollte es die Textart auch über das das gewählte image geben~~

Lange Erklärtexte mittes Hilfe-Dialog anzeigen.




Wenn das geschafft ist, kann file-upload ersatzlos entfernt werden.