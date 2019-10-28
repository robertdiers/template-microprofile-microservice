#set JAVA_HOME
#export JAVA_HOME=/home/developer/Documents/java/jdk1.8.0_151

#download payara
if [ -f "apache-tomee-8.0.0-M2-microprofile.tar.gz" ]
then
	echo "TomEE 8 Microprofile found."
else
	echo "TomEE 8 Microprofile download: "
    curl "http://artfiles.org/apache.org/tomee/tomee-8.0.0-M2/apache-tomee-8.0.0-M2-microprofile.tar.gz" --output "apache-tomee-8.0.0-M2-microprofile.tar.gz"    
    #extract TomEE
	tar -xf apache-tomee-8.0.0-M2-microprofile.tar.gz
fi

#set environment
export CATALINA_OPTS="-Dcom.bmw.mastersolutions.gf.profile=LOCAL"

#start exploded webapp
apache-tomee-microprofile-8.0.0-M2/bin/startup.sh
