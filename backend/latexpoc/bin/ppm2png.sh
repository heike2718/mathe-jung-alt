#!/bin/bash

# grafik ausschneiden, skalieren und in png umwandeln, dann temporäre Dateien löschen

pnmcrop $1.ppm | pnmscale 0.3 | pnmgamma 0.8 | pnmtopng > $1.png &&\
rm $1.aux &&\
rm $1.dvi &&\
rm $1.log &&\
rm $1.out &&\
rm $1.ppm &&\
rm $1.ps
