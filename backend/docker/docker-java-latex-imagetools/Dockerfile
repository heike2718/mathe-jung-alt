# syntax=docker/dockerfile:experimental
FROM heike2718/docker-java-latex:latest

COPY deb/*.deb /tmp/
COPY deb/*.sh /tmp/

# install image manipulation program
 RUN echo "install package netbpm" &&\
 . /tmp/install-packages.sh

# cleanup packages and install-script
RUN echo "cleanup packages and install-script." &&\
   rm /tmp/*.deb &&\
   rm /tmp/install-packages.sh
