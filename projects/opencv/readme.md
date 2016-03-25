Open Computer Vision
======

OpenCV can be used from Java to process images. You can do things like face recognition, line detection and image manipulation. It was originally created by Intel for Stanford's autonomous car. Stanley won the DARPA grand challenge by finishing the course in the shortest time. If you don't know what the grand challenge is, go to wikipedia.

Installation
------
The instructions have been tested with Noob 1.8.0, Noob 1.9.0, Raspberry Pi 1 B+, Pi 2 B and Pi 3. There's are two ways. Install the prebuilt binaries is the easy path. Building from source is the hard path and can lead you down a rabbit hole.

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

1. run dependency.sh to install the required dependency libraries
2. run install.sh script

This will take 5-6 hours to complete on Raspberry Pi 1 B+ or 1.5 hours on Raspberry Pi 2 B. The script starts by using apt-get to install the required libraries. When prompted by the installer, answer yes. When all the required libraries are done, it will run cmake to configure unix make. Make will compile the code, which takes the bulk of the time. Once make is done, make install will copy the binaries to /usr/local directory. 


Tips
------


