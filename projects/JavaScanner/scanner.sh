#!/bin/sh

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/

HOME_PATH=.
echo $HOME_PATH

CLASSPATH=
for i in `ls -1 $HOME_PATH/*.jar`
do
  CLASSPATH=${CLASSPATH}:${i}
done

if [ -n "$JAVA_HOME" ]; then
    JAVA="$JAVA_HOME/java"
else
    JAVA=java
fi

echo $CLASSPATH

echo $JAVA -classpath $CLASSPATH -Djava.library.path=. org.woolfel.cv.ScanTest $@
$JAVA -server -classpath $CLASSPATH -Djava.library.path=. org.woolfel.cv.ScanTest $@

