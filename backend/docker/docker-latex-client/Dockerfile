# syntax=docker/dockerfile:experimental
FROM heike2718/docker-java-latex-imagetools:latest

# copy the latex-client
COPY deployment/*.jar /opt/

# RUN . /bin/start-latex-client.sh

ENTRYPOINT [ "/bin/start-latex-client.sh" ]
