Java Open Computer Vision
======

This project uses OpenCV with Java to process video and images from the Raspberry Pi camera. You can do things like face recognition, object detection and motion tracking. OpenCV was originally created for Stanley autonomous vehicle. Stanford won the grand challenge by completing the course in the shortest time. If you don't know what the grand challenge is, go to wikipedia.

Installation
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


