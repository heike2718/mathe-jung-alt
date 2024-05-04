#!/bin/bash

# grafik ausschneiden und in svg umwandeln, dann temporäre Dateien löschen
#
# nur noch zur demo.
# autotrace wurde wieder herausgenommen, da es zu viele Abhängigkeiten hat und SVG sich im frontend nicht bewährt
#

convert $1.ppm -trim +repage $1-cropped.ppm &&\
echo "ppm cropped" &&\
autotrace $1-cropped.ppm --output-file=$1.svg --output-format=svg &&\
echo "cropped ppm converted to svg with autotrace" &&\
rm $1.aux $1.dvi $1.log $1.ppm $1.-cropped-ppm $1.ps &&\
#rm $1.out &&\
echo "======="
echo "done :)"
echo "======="