FROM thomasweise/docker-texlive-full:latest

# RUN apt-get install -y libnetpbm10-dev

COPY deb/*.deb /tmp/
COPY --chmod=766 deb/*.sh /tmp/
COPY --chmod=766 bin/*.sh /bin/
COPY --chmod=766 deb/*.sh /tmp/

# install image manipulation program
RUN echo "install package netbpm" &&\
 . /tmp/install-packages.sh

# cleanup packages and install-script
RUN echo "cleanup packages and install-script." &&\
    rm /tmp/*.deb &&\
    rm /tmp/install-packages.sh


# CMD ["bash"]
# ENTRYPOINT ["/bin/bash"]

