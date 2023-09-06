#!/bin/bash

# wandelt ein tex-file in ein pdf um
# --shell-escape sorgt dafür, dass der LaTeX-Compiler epstopdf aufrufen kann.

pdflatex --shell-escape -interaction=nonstopmode $1.tex
