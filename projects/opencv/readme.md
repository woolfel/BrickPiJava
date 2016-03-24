Open Computer Vision
======

If you want to use OpenCV with Java to process video and images from the Raspberry Pi camera, you will need to install OpenCV. Open Computer Vision is a set of libraries for processing images to extract features. It was originally created by Intel for Stanford's Stanley autonomous vehicle which won the grand challenge by completing the course in the shortest time. If you don't know what the grand challenge is, go to wikipedia.

Installation
------
These instructions have been tested with Noob 1.8.0, Noob 1.9.0, Raspberry Pi 1 B+ and Pi 2 B. There's the easy path using prebuilt binaries or build it from source. Before you try building from source, try the easy path first. Building from source can lead you down a rabbit hole hunting for dependencies.

<b>Easy Path</b>
------
In a terminal window run ./setup.sh

This will install all opencv related binaries from the public repositories. If the test doesn't work, you will need to build from source. Clone the BrickPi_Python from Github https://github.com/DexterInd/BrickPi_Python . In the Project_Examples/openCV folder is a few sample scripts. To verify it works, run

sudo python opencv_example.py

If it doesn't run, it means opencv isn't installed correctly.

<b>Build From Source</b>
------

Follow these instructions to install OpenCV.
![OpenCV source code](http://sourceforge.net/projects/opencvlibrary/files/opencv-unix/ "")

1. run install.sh script

This will take 5-6 hours to complete on Raspberry Pi 1 B+ or 1.5 hours on Raspberry Pi 2 B. The script starts by using apt-get to install the required libraries. When prompted by the installer, answer yes. When all the required libraries are done, it will run cmake to configure unix make. Make will compile the code, which takes the bulk of the time. Once make is done, make install will copy the binaries to /usr/local directory. 

How it works
------
To test the installation works correctly, run one of the examples like this:


Tips
------


