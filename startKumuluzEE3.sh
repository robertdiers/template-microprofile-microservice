#set JAVA_HOME
#export JAVA_HOME=/home/developer/Documents/java/jdk1.8.0_151

#set environment (PROD/INT/TEST) and run fat jar
$JAVA_HOME/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=9009,server=y,suspend=n -Dcom.bmw.mastersolutions.gf.profile=LOCAL -jar target/microprofile-microservice.jar
