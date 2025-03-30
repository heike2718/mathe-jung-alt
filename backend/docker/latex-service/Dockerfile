# syntax=docker/dockerfile:experimental
# 
# docker image build -t heike2718/latex-service-api:2.0 .
# docker container run -it heike2718/latex-service-api:2.0 bash
#
#FROM heike2718/docker-java-latex:latest
FROM heike2718/docker-java-latex:2.0

# install image manipulation program
COPY deb/*.deb /tmp/
COPY deb/*.sh /tmp/

 RUN echo "install package netbpm" &&\
 . /tmp/install-packages.sh

# cleanup packages and install-script
RUN echo "cleanup packages and install-script." &&\
   rm /tmp/*.deb &&\
   rm /tmp/install-packages.sh

# copy fonts fondsmap etc to /usr/local/share/texmf/
COPY ./usr/local/share/texmf/ /usr/local/share/texmf/

# copy the latex-client
COPY deployment/*.jar /opt/

COPY bin/ /bin/

RUN chown 1001 /bin/latex2*.sh &&\
    chown 1001 /bin/*ppm*.sh &&\
    chmod 766 /bin/latex2*.sh &&\
    chmod 766 /bin/*ppm*.sh &&\
    chown 1001 /bin/start-latex-client.sh &&\
    chmod 766 /bin/start-latex-client.sh &&\
    texhash

ENTRYPOINT [ "/bin/start-latex-client.sh" ]
