# Script-Name: ClientStart
# Startet den Client-Server und verbindet den Client mit dem Server
#Kompiliere Client.java mittels rmi-compiler
rmic Client
# Starten der Registry auf dem Server auf dem Port 9090
rmiregistry 9090 & 
# Server bzw. Client starten
java -Djava.security.policy=./java.policy.test Client
