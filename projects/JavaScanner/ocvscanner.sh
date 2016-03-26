#!/bin/sh

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib/jni/

HOME_PATH=.
echo $HOME_PATH

CLASSPATH=
for i in `ls -1 $HOME_PATH/*.jar`
do
  CLASSPATH=${CLASSPATH}:${i}
done

# add the opencv jar
CLASSPATH=$CLASSPATH:/usr/share/java/opencv-249.jar

if [ -n "$JAVA_HOME" ]; then
    JAVA="$JAVA_HOME/java"
else
    JAVA=java
fi

echo $CLASSPATH

echo $JAVA -classpath $CLASSPATH -Djava.library.path=$LD_LIBRARY_PATH org.woolfel.cv.OCVScanTest $@
$JAVA -server -classpath $CLASSPATH -Djava.library.path=$LD_LIBRARY_PATH org.woolfel.cv.OCVScanTest $@

