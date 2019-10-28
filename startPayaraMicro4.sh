#set JAVA_HOME
#export JAVA_HOME=/home/developer/Documents/java/jdk1.8.0_151

#download payara
if [ -f "payara-micro-4.1.2.181.jar" ]
then
	echo "payara found."
else
	echo "payara download: "
    curl "http://central.maven.org/maven2/fish/payara/extras/payara-micro/4.1.2.181/payara-micro-4.1.2.181.jar" --output "payara-micro-4.1.2.181.jar"
fi

#set environment (PROD/INT/TEST) and run exploded webapp
$JAVA_HOME/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,address=9009,server=y,suspend=n -Dcom.bmw.mastersolutions.gf.profile=LOCAL -jar payara-micro-4.1.2.181.jar --deploy deploymentPayaraMicro4/microprofile-microservice --port 8080
