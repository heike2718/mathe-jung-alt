# LaTeX für Java

Dieses Projekt enthält das Dockerimage für eine LaTeX-full-Installation, die durch ein kleines feines undertow-Servlet über Java das Compilieren und
umwandeln von LaTeX- Files anstoßen kann.

## Installation

### Image heike2718/docker-java-latex

Muss zuerst gebaut werden.

Es basiert auf einer TexLive-full- Installation von
[Thomas Weise](http://github.com/thomasWeise/docker-texlive-full) und erweitert diese um ein Open-JDK-17

__Achtung:__ Ich habe keine halben Sachen gemacht, damit später nicht irgendwelche LaTeX-Packages fehlen. Die full-installation dauert eine ganze Weile.

### Der Java-basierte LaTeX-Service - heike2718/latex-service-api

Dieses Image basiert auf heike2718/docker-java-latex, also dem Container mit texlive-full und einer JRE (aktuell Java 17).

und erweitert es um 

* imagetools, mit denen aus ps pngs generiert werden können
* Erstleserfreundliche Fonts 
* eine undertow-basierte kleine Java-Applikation, die ein REST-Backend zum Aufruf der Tools zum generieren von pdf und png aus LaTeX-Dateien zur Verfügung stellt.
  

### Testen des docker-latex-clients

Ist [hier](../../documentation/usage/latex-mircroservice.adoc) beschrieben.

