@echo off

rem Uncomment and set if  not set by the calling environment
rem JAVA_HOME=

rem JVM arguments - remote debug argument
rem set "JAVA_OPTS=%JAVA_OPTS% -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

rem # JVM arguments - GC options
rem set "JAVA_OPTS=%JAVA_OPTS% -XX:+UseParNewGC -XX:+UseConcMarkSweepGC"

rem # JVM arguments - print startup JVM argument
set "JAVA_OPTS=%JAVA_OPTS% -XX:+PrintCommandLineFlags"

rem # Start Server
"%JAVA_HOME%/bin/java" %JAVA_OPTS% -cp ../webapp/WEB-INF/lib/* sample.grocery.store.server.ServiceLauncher