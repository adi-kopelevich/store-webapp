#!/bin/sh

# Set the log dir path
logsDir=../logs
gcLogFile=$logsDir/gc.log

# Create the logs directory
mkdir -p -- "$gcLogFile"

# Should be set in case  not set by env
#JAVA_HOME=

# JVM arguments - GC log options.
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:-UseGCLogFileRotation -Xloggc:$gcLogFile"

# JVM arguments - GC options. using parallel CPU(s) for young (with desired pause time) & concurrent mark and sweep for old gen)
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC -XX:MaxGCPauseMillis=200 -XX:+UseConcMarkSweepGC"

# JVM arguments - Heap dump on OutOfMemory.
JAVA_OPTS=$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$logsDir"

# JVM arguments - remote debug argument
JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# JVM arguments - enable jmx remote access
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

# JVM arguments - print startup JVM argument
JAVA_OPTS="$JAVA_OPTS -XX:+PrintCommandLineFlags"

# Start Server
"$JAVA_HOME/bin/java" $JAVA_OPTS -jar ../webapp/WEB-INF/lib/task-list-runner-1.0-SNAPSHOT.jar
