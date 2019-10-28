# WebApp Eclipse Microprofile Microservice

Microservice Template based on Eclipse Microprofile.

* Please select one of the available application server and add JAVA_HOME to start*.sh script
* Please replace pom.xml with pom_kumuluzee3.xml when using KumuluzEE
* Please replace pom.xml with pom_thorntail2.xml when using Thorntail
* Please edit pom.xml to comment unused redeploy scripts (not relevant for KumuluzEE and Thorntail)


**AVAILABLE BUILDS**

* Maven
* Gradle


**FEATURES**

* example REST endpoint
* example timer task
* example JUnit test
* logging
* configuration
* request execution time
* startup and shutdown handling (or deployment / undeployment)


**AVAILABLE LOCAL SERVERS**

* Payara Micro 4 (WAR)
* TomEE 8 MicroProfile (WAR)
* WildFly 16 (WAR)
* KumuluzEE 3 (JAR + fat jar, modular, lightweight)
* Thorntail 2 (WAR/JAR + fat jar, modular)


**WINDOWS USERS**

Please use Cygwin or Git Bash to execute shell commands.
