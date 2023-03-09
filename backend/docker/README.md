# LaTeX für Java

Dieses Projekt enthält das Dockerimage für eine LaTeX-full-Installation, die durch ein kleines feines undertow-Servlet über Java das Compilieren und
umwandeln von LaTeX- Files anstoßen kann.

## Installation

### Image heike2718/docker-java-latex

Muss zuerst gebau werden.

Es basiert auf einer TexLive-full- Installation von
[Thomas Weise](http://github.com/thomasWeise/docker-texlive-full) und erweitert diese um ein Open-JDK-11

__Achtung:__ Ich habe keine halben Sachen gemacht, damit später nicht irgendwelche LaTeX-Packages fehlen. Die full-installation dauert eine ganze Weile.

### heike2718/docker-java-latex-imagetools

Wenn man gern aus den Compilaten des Basis-Images png generieren möchte, benötigt man diverse Image-Tools, die mit diesem Image hinzugefügt werden.

Das Image wird wie folgt gebaut:

```
DOCKER_BUILDKIT=1 docker image build -t heike2718/docker-java-latex-imagetools .
```

Wenn man das auf einem V-Server tut, auf dem schon einige Container laufen, kann es zu Fehlern der Art

```
 > [5/6] RUN echo "install package netbpm" && . /tmp/install-packages.sh:
#12 1.108 parent closed synchronisation channel
#12 1.395 container_linux.go:370: starting container process caused: process_linux.go:459: container init caused: Running hook #0:: error running hook: exit status 2, stdout: , stderr: runtime/cgo: pthread_create failed: Resource temporarily unavailable
...
```

kommen. Alle Container zu stoppen, hat geholfen.

### Der Java-Client - heike2718/docker-latex-client

heike2718/docker-java-latex-imagetools stellt eine sehr einfache API für die Kommandozeile zur Verfügung. Im Wesentlichen ist das pdflatex und ein
Shell-Script zum Umwandeln von ps in pngs.

Der Java-Client ist ein Docker-Image, das diese API aus Java heraus ansprechen kann.
