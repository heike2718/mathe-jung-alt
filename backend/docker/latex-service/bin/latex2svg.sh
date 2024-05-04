#!/bin/bash

# wandelt ein tex-file in ein svg um
#
# nur noch zur demo.
# autotrace wurde wieder herausgenommen, da es zu viele Abhängigkeiten hat und SVG sich im frontend nicht bewährt
#

cd /doc &&\
latex -interaction=nonstopmode $1.tex &&\
dvips $1.dvi &&\
. /bin/ps2ppm.sh $1 &&\
. /bin/ppm2svg.sh $1
