#! /bin/bash

# for Raspberry Pi 1 B+ and older Noob java 1.7 is installed, change the JAVA_HOME
# export JAVA_HOME=/usr/lib/jvm/jdk-7-oracle-armhf
export JAVA_HOME=/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt

sudo apt-get install build-essential
sudo apt-get install python-dev python-numpy
sudo apt-get install libavcodec-dev libavformat-dev libswscale-dev
sudo apt-get install libjpeg-dev libpng-dev libtiff-dev libjasper-dev
sudo apt-get install python-scipy python-matplotlib libgtk2.0-dev
sudo apt-get install cmake
sudo apt-get install ant