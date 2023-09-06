#!/bin/bash

# ruft den docker-latex-container mit dem skript latex2png auf

docker container exec latex /bin/latex2png.sh $1
