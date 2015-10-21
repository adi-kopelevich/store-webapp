#!/bin/sh

# Should be set in case  not set by env
#JAVA_HOME=

# JVM arguments - remote debug argument
JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# JVM arguments - GC options. using parallel CPU(s) for young gen & concurent mark and sweep for old gen)
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC -XX:+UseConcMarkSweepGC –XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:../logs/gc.log -XX:-UseGCLogFileRotation"

# JVM arguments - print startup JVM argument
JAVA_OPTS="$JAVA_OPTS -XX:+PrintCommandLineFlags"

# JVM arguments - enable jmx remote access
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

# Start Server
"$JAVA_HOME/bin/java" $JAVA_OPTS -jar ../webapp/WEB-INF/lib/task-list-runner-1.0-SNAPSHOT.jar
