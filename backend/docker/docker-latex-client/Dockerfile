# syntax=docker/dockerfile:experimental
FROM heike2718/docker-java-latex-imagetools:latest

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

# RUN . /bin/start-latex-client.sh

ENTRYPOINT [ "/bin/start-latex-client.sh" ]
