FROM heike2718/docker-java-latex:latest

# RUN apt-get install -y libnetpbm10-dev

# RUN apt-get install -y openjdk-11-jre-headless

COPY deb/*.deb /tmp/
COPY --chmod=766 deb/*.sh /tmp/
COPY --chmod=766 bin/*.sh /bin/
COPY --chmod=766 deb/*.sh /tmp/

# install image manipulation program
 RUN echo "install package netbpm" &&\
 . /tmp/install-packages.sh

# copy the latex-client
COPY jar/*.jar /opt/

# cleanup packages and install-script
RUN echo "cleanup packages and install-script." &&\
   rm /tmp/*.deb &&\
   rm /tmp/install-packages.sh

# RUN . /bin/start-latex-client.sh


ENTRYPOINT [ "/bin/start-latex-client.sh" ]
