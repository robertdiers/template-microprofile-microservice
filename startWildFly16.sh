#set JAVA_HOME
#export JAVA_HOME=/home/developer/Documents/java/jdk1.8.0_151

#download payara
if [ -f "wildfly-16.0.0.Final.tar.gz" ]
then
	echo "WildFly 16 found."
else
	echo "WildFly 16 download: "
    curl "https://download.jboss.org/wildfly/16.0.0.Final/wildfly-16.0.0.Final.tar.gz" --output "wildfly-16.0.0.Final.tar.gz"    
    #extract WildFly
	tar -xf wildfly-16.0.0.Final.tar.gz
fi

#set environment
export JAVA_OPTS="-Dcom.bmw.mastersolutions.gf.profile=LOCAL"

#start exploded webapp
wildfly-16.0.0.Final/bin/standalone.sh
