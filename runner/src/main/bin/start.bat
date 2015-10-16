@echo off

rem Should be set in case  not set by env
rem JAVA_HOME=

rem JVM arguments - remote debug argument
set "JAVA_OPTS=%JAVA_OPTS% -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

rem JVM arguments - GC options
set "JAVA_OPTS=%JAVA_OPTS% -XX:+UseParNewGC -XX:+UseConcMarkSweepGC"

rem JVM arguments - enable jmx remote access
set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false

rem JVM arguments - print startup JVM argument
set "JAVA_OPTS=%JAVA_OPTS% -XX:+PrintCommandLineFlags"

rem Start Server
"%JAVA_HOME%/bin/java" %JAVA_OPTS% -jar ../webapp/WEB-INF/lib/store-runner-1.0-SNAPSHOT.jar
