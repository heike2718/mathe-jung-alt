#!/bin/bash

# ps in ppm umwandeln

gs -sDEVICE=ppmraw -r300 -q -dNOPAUSE -dBATCH -sOutputFile=$1.ppm $1.ps
