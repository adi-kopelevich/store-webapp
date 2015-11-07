task-list-app
=======================
Simple Task List Application for managing a list of To-do items.
Served by simple JS UI (JQuery) and a JAX-RS REST service (Jersey) implementation working against MongoDB(default)/In-memory for persistence.
Deployed using a Webapp context handler on top of a lean embedded Jetty server.

The application consists the following directories:

    /bin    - start/stop bash scripts to manage the java process)
    /conf   - where general configuration can be manipulated (jetty, log4j, mongoDB, etc')
    /log    - logs directory (created on app usage) contains both server.log for application log messages and access log(s) for tracking traffic
    /webapp - a webapp directory, which holds the WEB-INF/web.xml, the java binaries (/lib/*.jar) and the UI static resources (index.html, /js/*.js, /css/*.css)

Getting started...
-----------------------

### Prerequisites

1. git client 
2. Maven 3.x and M2_HOME/bin included in PATH
3. JDK 7 (or above...), JAVA_HOME set appropriately and JAVA_HOME/bin included in path  
4. MongoDB local instance (please refer to app.properties under /conf dir in order to change or enable/disable)

### Build application artifacts 

1. $ mkdir task-list-app 
2. $ cd task-list-app 
3. $ git clone https://github.com/adi-kopelevich/task-list-app.git
4. $ mvn clean install

### Run application using bash scripts (instructions are for Linux...) 

1. $ cd /task-list-app/runner/target/task-list-app-1.0-SNAPSHOT
2. $ ./bin/start.sh 
(An alternative is to use the runner/target/task-list-app-1.0-SNAPSHOT.tar.gz to export to a desired location)

#### Please note:
In case you want the application to work In-memory instead of using a mongoDB instance for persistence, 
Please refer to app.properties under /conf dir in order to change the configuration appropriately before running start.sh] 

### Access the application using a browser 
http://localhost:8080/

### Access the application REST Resource using a browser/client
http://localhost:8080/rest/items/