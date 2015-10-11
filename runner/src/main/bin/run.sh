#!/bin/sh

# Uncomment and set in not set by the calling environment
#JAVA_HOME=

# JVM arguments - remote debug argument
#JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# JVM arguments - GC options
#JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC -XX:+UseConcMarkSweepGC"

# JVM arguments - print startup JVM argument
JAVA_OPTS="$JAVA_OPTS -XX:+PrintCommandLineFlags"

# Start Server
"$JAVA_HOME/bin/java" $JAVA_OPTS -cp ../webapp/WEB-INF/lib/* sample.grocery.store.server.ServiceLauncher
