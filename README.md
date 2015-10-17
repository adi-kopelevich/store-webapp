task-list-app
=======================
Simple Task List Application for managing a list of To-do(s)
Served by simple JS UI (JQuery) and JAX-RS REST service (Jersey) working against MongoDB for persistence
Deployed using webapp context handler on top of a lean embedded Jetty server

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
4. MongoDB local instance (please refer to mongo.properties under /conf dir in order to change)

### Build application artifacts 

1. $ mkdir task-list-app 
2. $ cd task-list-app 
3. $ git clone https://github.com/adi-kopelevich/task-list-app.git
4. $ mvn clean install

### Run application using bash scripts (instructions are for Linux...) 

1. $ mkdir app
3. $ tar -xvf ./runner/task-list-app-1.0-SNAPSHOT.tar.gz --directory ./app
4. $ ./app/bin/start.sh 

### Access application using a browser/client 

* UI Index.html - http://localhost:8080/
* REST Resource - http://localhost:8080/rest/items/



 



