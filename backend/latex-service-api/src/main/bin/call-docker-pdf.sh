#!/bin/bash

# ruft den docker-latex-container mit dem skript latex2pdf auf

docker container exec latex /bin/latex2pdf.sh $1
