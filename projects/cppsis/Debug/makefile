######################################################################
# Note: the make expects the source files to be in a subfolder named "src"
######################################################################

-include ../makefile.init
-include ../makefile.defs

all:
	g++ -I/usr/local/include/eigen -I/usr/local/include/opencv -I/usr/local/include/opencv2 -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"src/LineScanner.d" -MT"src/LineScanner.d" -o "src/LineScanner.o" "../src/LineScanner.cpp"
	g++ -I/usr/local/include/eigen -I/usr/local/include/opencv -I/usr/local/include/opencv2 -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"src/ScannedColumn.d" -MT"src/ScannedColumn.d" -o "src/ScannedColumn.o" "../src/ScannedColumn.cpp"
	g++ -I/usr/local/include/eigen -I/usr/local/include/opencv -I/usr/local/include/opencv2 -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"src/cppsis.d" -MT"src/cppsis.d" -o "src/cppsis.o" "../src/cppsis.cpp"
	g++ -L/usr/local/lib -o "cppsis"  ./src/LineScanner.o ./src/ScannedColumn.o ./src/cppsis.o   -lopencv_core -lopencv_imgproc -lopencv_highgui
