# Image generation

```
latex mydoc.tex
dvips -o mydoc.ps mydoc.dvi
convert -density 300 mydoc.ps mydoc.ppm
pnmcrop mydoc.ppm | pnmscale 0.3 | pnmgamma 0.8 | pnmtopng > mydoc.png
```

## Umwandeln in SVG

### Erzeugen eines ppm

```
gs -sDEVICE=ppmraw -r300 -q -dNOPAUSE -dBATCH -sOutputFile=augabe.ppm aufgabe.ps
gs -sDEVICE=ppmraw -r300 -q -dNOPAUSE -dBATCH -sOutputFile=komplex.ppm komplex.ps
```

Wichtg: es darf keine Seitennummer dabei sein, beim Generieren des ps, also in LaTeX __\pagestyle{empty}__

### Zurechtschneiden des ppm mit imagemagic convert

```
#pnmcrop aufgabe.ppm | pnmgamma 0.8 | pnmtopng > aufgabe-cropped.ppm
convert komplex.ppm -trim +repage komplex-cropped.ppm
```

Beachte: verlustfrei - kein Scaling

### Umwandeln von ppm in svg mit potrace

__install potrace__

```
sudo apt-get install potrace
```

__convert ppm to svg with potrace__

```
# convert ppm to svg with potrace
potrace -s aufgabe-cropped.ppm -o aufgabe.svg
potrace -s komplex-cropped.ppm -o komplex.svg
```

__convert to base64__

```
# convert to base64 without linebreaks
base64 -w 0 aufgabe.svg > aufgabe-base64.txt
base64 -w 0 komplex.svg > komplex-base64.txt
```

## potrace funktioniert leider nicht mit Graustufen

### Installation von autotrace

1. [gitrepo](https://github.com/autotrace/autotrace) clonen
2. ins Repo wechseln
3. sudo apt-get -y install intltool
4. sudo apt-get install autopoint
5. bash autogen.sh

```
autotrace C-5.ppm --output-file=C-5-autotraced.svg --output-format=svg
base64 -w 0 C-5-autotraced.svg > C-5-autotraced-base64.txt

```

```
autotrace C-5.ppm --output-file=C-5-3.svg --output-format=svg
base64 -w 0 C-5-3.svg > C-5-3-base64.txt

```

Tests hier:

/home/heike/docker-volumes/latex/doc/vorschau/test/minikaenguru_2020_klasse_2_9034f6eb
/home/heike/work/images-experimente/minikaenguru_2020_klasse_2_28a80fae/02611.tex