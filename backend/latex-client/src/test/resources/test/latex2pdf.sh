#!/bin/bash

# wandelt ein tex-file in ein pdf um

pdflatex -interaction=nonstopmode $1.tex
