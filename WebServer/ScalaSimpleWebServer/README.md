Implémentation du SimpleWebServer en Scala (version 2.12 pour java 1.8) à télécharger sur http://www.scala-lang.org/news/2.12.0/, qui utilise les acteurs Akka.


   pour compiler : 
```
scalac -classpath ./akka-actor_2.12-2.5.10.jar SimpleWebServer.scala
```
   pour exécuter :
```
scala -classpath .:./akka-actor_2.12-2.5.10.jar:./config-1.3.3.jar SimpleWebServer 8080 test.html
```
