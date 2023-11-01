# Woran ich gerade arbeite

Damit ich mich nach einigen Tagen Pause wieder erinnere, wo ich weitermachen muss.

## 01.11.2024

Arbeite an [Relativen Pfad und includegraphics beim Hochladen von Grafiken generieren](https://github.com/heike2718/mathe-jung-alt/issues/96)

Die raetsel-details.component ist soweit fertig.

In der grafik-details.component wurde die file-upload.component durch eine Kombination von select-file.component und file-info.component
ersetzt, analog zur raetsel-editor.

Zum Hochladen wird der grafik.state und die GrafikFacade verwendet. 

grafik.state muss in embeddableImages.state integriert werden und alle Methode aus GrafikFacade müssen in EmbeddableImagesFacade umziehen.

Nach dem Hochladen der neuen Grafik wird das Vorschaubild in der grafik-details.component noch nicht ausgetauscht.
Das Event für das erfolgreiche Hochladen gelangt noch nicht zurück in die grafik-details.component. 

Also die GRAFIK_HOCHGELADEN - Action sollte das Laden der Vorschau auslösen. Da ist uns ein Event abhanden gekommen, das im file-upload noch 
vorhanden war.

Versucht, das über das aneinanderketten von actions in grafik.effects und grafik.reducer zu lösen, aber:

__Es klappt noch nicht.__

Falls die Grafik zum Pfad nicht existiert, wird jetzt schon korrekt createEmbeddableImage aufgerufen, aber der neue Pfad muss
es zurück ins Rätsel schaffen, also in die embeddableImageInfos. 

Das wird noch trickreich.

Wenn das geschafft ist, kann file-upload ersatzlos entfernt werden.