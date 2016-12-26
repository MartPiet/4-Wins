# Script-Name: ServerStart
# Startet den Server für das Vier Gewinnt Spiel
javac Server.java
# Kompilierung mittels RMI-Compiler:
rmic Server 
#Registry auf dem Port 9090 starten:
 rmiregistry 9090 &
#Server starten:
java -Djava.security.policy=./java.policy.test
       Server
