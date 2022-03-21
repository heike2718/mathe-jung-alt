#!/bin/bash

# wandelt ein tex-file in ein pdf um

cd /doc &&\
pdflatex $1.tex &&\
rm $1.aux &&\
rm $1.log &&\
rm $1.out
