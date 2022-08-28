#!/bin/bash

# wandelt ein tex-file in ein png um

cd /doc &&\
latex -interaction=nonstopmode $1.tex &&\
dvips $1.dvi &&\
. /bin/ps2ppm.sh $1 &&\
. /bin/ppm2png.sh $1
