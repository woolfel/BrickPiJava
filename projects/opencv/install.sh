#! /bin/bash

# for Raspberry Pi 1 B+ and older Noob java 1.7 is installed, change the JAVA_HOME
# export JAVA_HOME=/usr/lib/jvm/jdk-7-oracle-armhf
export JAVA_HOME=/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt

wget http://sourceforge.net/projects/opencvlibrary/files/opencv-unix/2.4.9/opencv-2.4.9.zip
unzip opencv*
cd opencv*
mkdir release
cd release
sudo cmake -D CMAKE_BUILD_TYPE=RELEASE -D WITH_OPENCL=OFF -D BUILD_PERF_TESTS=OFF -DJAVA_INCLUDE_PATH=$JAVA_HOME/include -DJAVA_AWT_LIBRARY=$JAVA_HOME/jre/lib/amd64/libawt.so -DJAVA_JVM_LIBRARY=$JAVA_HOME/jre/lib/arm/server/libjvm.so -D CMAKE_INSTALL_PREFIX=/usr/local ..
sudo make
sudo make install

