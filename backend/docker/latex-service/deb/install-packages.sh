#!/bin/bash

# this script installs netbpm for pnm-tools
# autotrace wurde wieder herausgenommen, da es zu viele Abhängigkeiten hat und SVG sich im frontend nicht bewährt

apt-get install -y /tmp/libnetpbm10_10.0-15.3+b2_amd64.deb
apt-get install -y /tmp/libjpeg62-turbo_1.5.2-2+deb10u1_amd64.deb
apt-get install -y /tmp/netpbm_10.0-15.3+b2_amd64.deb
#apt-get install -y /tmp/autotrace_0.40.0-20240428_all.deb
