#! /bin/bash

export JAVA_HOME=/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt

sudo apt-get install build-essential
sudo apt-get install python-dev python-numpy
sudo apt-get install libavcodec-dev libavformat-dev libswscale-dev
sudo apt-get install libjpeg-dev libpng-dev libtiff-dev libjasper-dev
sudo apt-get install python-scipy python-matplotlib libgtk2.0-dev
sudo apt-get install cmake

cd ~
sudo wget http://sourceforge.net/projects/opencvlibrary/files/opencv-unix/2.4.10/opencv-2.4.10.zip
sudo unzip opencv*
sudo cd opencv*
sudo mkdir release
cd release
cmake -D CMAKE_BUILD_TYPE=RELEASE -D WITH_OPENCL=OFF -D BUILD_PERF_TESTS=OFF -DJAVA_INCLUDE_PATH=$JAVA_HOME/include -DJAVA_AWT_LIBRARY=$JAVA_HOME/jre/lib/amd64/libawt.so -DJAVA_JVM_LIBRARY=$JAVA_HOME/jre/lib/arm/server/libjvm.so -D CMAKE_INSTALL_PREFIX=/usr/local ..
sudo make
sudo make install

